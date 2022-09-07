class BackupThread extends Thread {
    public void prepare() throws ReplicatorException, InterruptedException {
        logger.info(String.format("Using directory '%s' for replicator logs", logDirName));
        logger.info("Checksums enabled for log records: " + doChecksum);
        if (logger.isDebugEnabled()) {
            logger.debug("logFileSize = " + logFileSize);
        }
        logDir = new File(logDirName);
        if (!logDir.exists()) {
            if (readOnly) {
                throw new ReplicatorException("Log directory does not exist : " + logDir.getAbsolutePath());
            } else {
                logger.info("Log directory does not exist; creating now:" + logDir.getAbsolutePath());
                if (!logDir.mkdirs()) {
                    throw new ReplicatorException("Unable to create log directory: " + logDir.getAbsolutePath());
                }
            }
        }
        if (!logDir.isDirectory()) {
            throw new ReplicatorException("Log directory is not a directory: " + logDir.getAbsolutePath());
        }
        if (readOnly) {
            logger.info("Using read-only log connection");
        } else {
            if (!logDir.canWrite()) {
                throw new ReplicatorException("Log directory is not writable: " + logDir.getAbsolutePath());
            }
            File lockFile = new File(logDir, "disklog.lck");
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to acquire lock on write lock file: " + lockFile.getAbsolutePath());
            }
            writeLock = new WriteLock(lockFile);
            writeLock.acquire();
            if (writeLock.isLocked()) logger.info("Acquired write lock; log is writable"); else logger.info("Unable to acquire write lock; log is read-only");
        }
        try {
            eventSerializer = (Serializer) Class.forName(eventSerializerClass).newInstance();
        } catch (Exception e) {
            throw new ReplicatorException("Unable to load event serializer class: " + eventSerializerClass, e);
        }
        logger.info("Loaded event serializer class: " + eventSerializer.getClass().getName());
        if (listLogFiles(logDir, DATA_FILENAME_PREFIX).length == 0) {
            if (readOnly) {
                throw new ReplicatorException("Attempting to read a non-existent log; is log initialized? dirName=" + logDir.getAbsolutePath());
            } else {
                String logFileName = getDataFileName(fileIndex);
                LogFile logFile = new LogFile(logDir, logFileName);
                logFile.setBufferSize(bufferSize);
                logger.info("Initializing logs: logDir=" + logDir.getAbsolutePath() + " file=" + logFile.getFile().getName());
                logFile.create(-1);
                logFile.close();
            }
        }
        if (logger.isDebugEnabled()) logger.debug("Preparing index");
        index = new LogIndex(logDir, DATA_FILENAME_PREFIX, logFileRetainMillis, bufferSize);
        LogFile logFile = openLastFile(readOnly);
        String logFileName = logFile.getFile().getName();
        int logFileIndexPos = logFile.getFile().getName().lastIndexOf(".");
        fileIndex = Long.valueOf(logFileName.substring(logFileIndexPos + 1));
        long maxSeqno = logFile.getBaseSeqno();
        long lastCompleteEventOffset = logFile.getOffset();
        boolean lastFrag = true;
        if (logger.isDebugEnabled()) logger.debug("Starting max seqno is " + maxSeqno);
        try {
            logger.info("Validating last log file: " + logFile.getFile().getAbsolutePath());
            LogRecord currentRecord = null;
            currentRecord = logFile.readRecord(0);
            byte lastRecordType = -1;
            while (!currentRecord.isEmpty()) {
                lastRecordType = currentRecord.getData()[0];
                if (lastRecordType == LogRecord.EVENT_REPL) {
                    LogEventReplReader eventReader = new LogEventReplReader(currentRecord, eventSerializer, doChecksum);
                    lastFrag = eventReader.isLastFrag();
                    if (lastFrag) {
                        maxSeqno = eventReader.getSeqno();
                        lastCompleteEventOffset = logFile.getOffset();
                    }
                    eventReader.done();
                } else if (lastRecordType == LogRecord.EVENT_ROTATE) {
                    String fileName = logFile.getFile().getName();
                    logger.info("Last log file ends on rotate log event: " + fileName);
                    logFile.close();
                    if (!readOnly) {
                        index.setMaxIndexedSeqno(maxSeqno);
                        logFileIndexPos = fileName.lastIndexOf(".");
                        fileIndex = Long.valueOf(fileName.substring(logFileIndexPos + 1));
                        fileIndex = (fileIndex + 1) % Integer.MAX_VALUE;
                        logFile = this.startNewLogFile(maxSeqno + 1);
                    }
                    break;
                }
                currentRecord = logFile.readRecord(0);
            }
            index.setMaxIndexedSeqno(maxSeqno);
            if (!readOnly && currentRecord.isTruncated()) {
                if (writeLock.isLocked()) {
                    logger.warn("Log file contains partially written record: offset=" + currentRecord.getOffset() + " partially written bytes=" + (logFile.getLength() - currentRecord.getOffset()));
                    logFile.setLength(currentRecord.getOffset());
                    logger.info("Log file truncated to end of last good record: length=" + logFile.getLength());
                } else {
                    logger.warn("Log ends with a partially written record " + "at end, but this log is read-only.  " + "It is possible that the process that " + "owns the write lock is still writing it.");
                }
            }
            if (!readOnly && !lastFrag) {
                if (writeLock.isLocked()) {
                    logger.warn("Log file contains partially written transaction; " + "truncating to last full transaction: seqno=" + maxSeqno + " length=" + lastCompleteEventOffset);
                    logFile.setLength(lastCompleteEventOffset);
                } else {
                    logger.warn("Log ends with a partially written " + "transaction, but this log is read-only.  " + "It is possible that the process that " + "owns the write lock is still writing it.");
                }
            }
        } catch (IOException e) {
            throw new ReplicatorException("I/O error while scanning log file: name=" + logFile.getFile().getAbsolutePath() + " offset=" + logFile.getOffset(), e);
        } finally {
            if (logFile != null) logFile.close();
        }
        logger.info("Setting up log flush policy: fsyncIntervalMillis=" + flushIntervalMillis + " fsyncOnFlush=" + this.fsyncOnFlush);
        if (!this.readOnly) {
            startLogSyncTask();
        }
        this.cursorManager = new LogCursorManager();
        cursorManager.setTimeoutMillis(logConnectionTimeoutMillis);
        logger.info(String.format("Idle log connection timeout: %dms", logConnectionTimeoutMillis));
        logger.info("Log preparation is complete");
    }
}
