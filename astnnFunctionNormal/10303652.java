class BackupThread extends Thread {
    @Override
    public void openImpl() throws FileIOException {
        int tryCount = 0;
        boolean done = false;
        while (!done) {
            try {
                randomAccessFile = new RandomAccessFile(file, MODE_RWS);
                done = true;
            } catch (Exception exception) {
                if (++tryCount >= maxRetry) {
                    final String message = FAILED_OPEN + file;
                    logger.error(message);
                    throw HELPER_FILE_UTIL.fileIOException(message, file, exception);
                }
                logger.warn(FAILED_OPEN + file);
                try {
                    Thread.sleep(retryMsDelay);
                } catch (InterruptedException exception2) {
                    final String message = "wait interrupted, failed open " + file;
                    logger.error(message);
                    throw HELPER_FILE_UTIL.fileIOException(message, file, exception);
                }
            }
        }
        tryCount = 0;
        done = false;
        while (!done) {
            try {
                fileLock = randomAccessFile.getChannel().lock();
                if (fileLock == null) {
                    throw new IOException("no lock");
                } else {
                    done = true;
                }
            } catch (IOException exception) {
                if (++tryCount >= maxRetry) {
                    try {
                        randomAccessFile.close();
                    } catch (Exception exception2) {
                        logger.warn("while closing after acquire lock failure", exception2);
                    }
                    randomAccessFile = null;
                    throw HELPER_FILE_UTIL.fileIOException("file " + file + " failed lock", file, exception);
                }
                try {
                    Thread.sleep(retryMsDelay);
                } catch (InterruptedException exception2) {
                    final String message = "wait interrupted, file " + file + " failed lock";
                    logger.error(message, exception2);
                    throw HELPER_FILE_UTIL.fileIOException(message, file, exception);
                }
            }
        }
        currentPositionInFile = 0;
        if (logger.debugEnabled) {
            logger.debug("open " + file);
        }
    }
}
