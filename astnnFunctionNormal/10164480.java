class BackupThread extends Thread {
    public void run() {
        try {
            long fileSize = 0;
            long lastZxidSeen = -1;
            FileChannel fc = null;
            while (true) {
                Request si = null;
                if (toFlush.isEmpty()) {
                    si = queuedRequests.take();
                } else {
                    si = queuedRequests.poll();
                    if (si == null) {
                        flush(toFlush);
                        continue;
                    }
                }
                if (si == requestOfDeath) {
                    break;
                }
                if (si != null) {
                    ZooTrace.logRequest(LOG, ZooTrace.CLIENT_REQUEST_TRACE_MASK, 'S', si, "");
                    TxnHeader hdr = si.hdr;
                    if (hdr != null) {
                        if (hdr.getZxid() <= lastZxidSeen) {
                            LOG.warn("Current zxid " + hdr.getZxid() + " is <= " + lastZxidSeen + " for " + hdr.getType());
                        }
                        Record txn = si.txn;
                        if (logStream == null) {
                            fileSize = 0;
                            logStream = new FileOutputStream(new File(zks.dataLogDir, ZooKeeperServer.getLogName(hdr.getZxid())));
                            synchronized (streamsToFlush) {
                                streamsToFlush.add(logStream);
                            }
                            fc = logStream.getChannel();
                            logArchive = BinaryOutputArchive.getArchive(logStream);
                        }
                        final long fsize = fileSize;
                        final FileChannel ffc = fc;
                        fileSize = Profiler.profile(new Profiler.Operation<Long>() {

                            public Long execute() throws Exception {
                                return SyncRequestProcessor.this.padLogFile(ffc, fsize);
                            }
                        }, PADDING_TIMEOUT, "Logfile padding exceeded time threshold");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        BinaryOutputArchive boa = BinaryOutputArchive.getArchive(baos);
                        hdr.serialize(boa, "hdr");
                        if (txn != null) {
                            txn.serialize(boa, "txn");
                        }
                        logArchive.writeBuffer(baos.toByteArray(), "txnEntry");
                        logArchive.writeByte((byte) 0x42, "EOR");
                        logCount++;
                        if (logCount > snapCount / 2 && r.nextInt(snapCount / 2) == 0) {
                            if (snapInProcess != null && snapInProcess.isAlive()) {
                                LOG.warn("Too busy to snap, skipping");
                            } else {
                                logStream = null;
                                logArchive = null;
                                snapInProcess = new Thread() {

                                    public void run() {
                                        try {
                                            zks.snapshot();
                                        } catch (Exception e) {
                                            LOG.warn("Unexpected exception", e);
                                        }
                                    }
                                };
                                snapInProcess.start();
                            }
                            logCount = 0;
                        }
                    }
                    toFlush.add(si);
                    if (toFlush.size() > 1000) {
                        flush(toFlush);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Severe error, exiting", e);
            System.exit(11);
        }
        ZooTrace.logTraceMessage(LOG, ZooTrace.getTextTraceLevel(), "SyncRequestProcessor exiyed!");
    }
}
