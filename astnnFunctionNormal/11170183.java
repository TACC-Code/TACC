class BackupThread extends Thread {
    private int writeStream(String key, InputStream sourceStream, String sourceName) throws CrossPatherException {
        int statusCode = -1;
        HttpClient client = prepareWriteHttpClient();
        PostMethod method = null;
        PerformanceLogger perf = new PerformanceLogger();
        perf.start();
        byte[] buffer = new byte[READ_BUFFER_SIZE];
        int readbytes = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int currentReadBytes = 0;
        int currentBatchNo = 0;
        try {
            String sendFileProgress = props.getProperty(PROPERTY_KEYS.SEND_FILE_PROGRESS);
            MyProgressListener myProgressListener;
            if (sendFileProgress != null && "ON".equals(sendFileProgress)) {
                myProgressListener = new MyProgressListener(key, true);
            } else {
                myProgressListener = new MyProgressListener(key, false);
            }
            do {
                currentReadBytes = 0;
                bos.reset();
                while (currentReadBytes < MAX_POST_BYTES_SIZE) {
                    try {
                        readbytes = sourceStream.read(buffer);
                    } catch (IOException e) {
                        boolean isCritical = fileSendErrorHandler(false, e, null, true, key);
                        if (isCritical) {
                            try {
                                sourceStream.close();
                            } catch (IOException e1) {
                                logger.doLog(AppLogger.WARN, "Error while closing stream", e1);
                            }
                            return -1;
                        }
                    }
                    currentReadBytes += readbytes;
                    if (readbytes == -1) {
                        break;
                    }
                    bos.write(buffer, 0, readbytes);
                    buffer = new byte[(currentReadBytes + READ_BUFFER_SIZE > MAX_POST_BYTES_SIZE) ? (MAX_POST_BYTES_SIZE - currentReadBytes) : READ_BUFFER_SIZE];
                }
                method = createMethodForStreaming(key, sourceName, readbytes, bos, currentReadBytes, currentBatchNo, myProgressListener);
                logger.doLog(AppLogger.DEBUG, "Sending New Batch: " + currentBatchNo + ", batchSize" + currentReadBytes + ", final:" + (readbytes == -1 ? true : false), null);
                fileServerListener.updateSubStatus("Sending New Batch: " + currentBatchNo + ", batchSize" + currentReadBytes + ", final:" + (readbytes == -1 ? true : false));
                statusCode = -1;
                boolean hasFailed = false;
                Throwable failureCause = null;
                boolean isCritical = false;
                int alreadySentBytes = myProgressListener.getCurrent();
                try {
                    perf.split("Before client.executeMethod()");
                    statusCode = client.executeMethod(method);
                    perf.split("After client.executeMethod()");
                    if (statusCode != HttpStatus.SC_OK) {
                        hasFailed = true;
                    }
                } catch (IOException e) {
                    hasFailed = true;
                    failureCause = e;
                }
                int retryCount = 0;
                while (hasFailed) {
                    client = prepareWriteHttpClient();
                    method = createMethodForStreaming(key, sourceName, readbytes, bos, currentReadBytes, currentBatchNo, myProgressListener);
                    if (statusCode != HttpStatus.SC_GATEWAY_TIMEOUT) {
                        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
                        params.setConnectionTimeout(30000);
                        params.setSoTimeout(30000);
                        client.getHttpConnectionManager().setParams(params);
                    }
                    if (failureCause == null) {
                        failureCause = ExceptionFactory.createException(ERROR_CODES.CONNECTION_ERROR, statusCode + "");
                    }
                    isCritical = fileSendErrorHandler(false, failureCause, null, false, key);
                    hasFailed = false;
                    failureCause = null;
                    if (!isCritical) {
                        myProgressListener.resetCurrentSize(alreadySentBytes);
                        try {
                            perf.split("Before client.executeMethod() - retry " + (++retryCount));
                            statusCode = client.executeMethod(method);
                            perf.split("After client.executeMethod() - retry " + retryCount);
                            if (statusCode != HttpStatus.SC_OK) {
                                hasFailed = true;
                            }
                        } catch (IOException e) {
                            hasFailed = true;
                            failureCause = e;
                        }
                    } else {
                        break;
                    }
                }
                if (isCritical) {
                    break;
                } else {
                    logger.doLog(AppLogger.DEBUG, "Successfully sent Batch: " + currentBatchNo + ", batchSize" + currentReadBytes + ", final:" + (readbytes == -1 ? true : false), null);
                    fileServerListener.updateSubStatus("Successfully sent Batch: " + currentBatchNo + ", batchSize" + currentReadBytes + ", final:" + (readbytes == -1 ? true : false));
                }
                currentBatchNo++;
            } while (readbytes != -1);
            perf.split("After Full File Upload  - Uploaded Bytes: " + myProgressListener.getCurrent());
        } finally {
            if (method != null) {
                method.releaseConnection();
                perf.split("After method.releaseConnection()");
            }
        }
        perf.stop();
        logger.doLog(AppLogger.DEBUG, "\n" + perf.getStats(), null);
        return statusCode;
    }
}
