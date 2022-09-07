class BackupThread extends Thread {
    public synchronized void runTask() {
        try {
            if (state == State.LOGIN || state == State.WAITING || state == State.COMMAND_UPLOAD) {
                if (state == State.WAITING && ((System.currentTimeMillis() - startTime) > TIME_OUT)) {
                    disconnect();
                }
                String cmd = reader.readToken();
                if (cmd != null) {
                    logger.debug("Got command - processing: " + cmd);
                    processCommand(cmd);
                    setActionPerformed();
                }
            } else if (state == State.COMMAND_DOWNLOAD) {
                if (downloadRequest != null) {
                    startDownload();
                    setActionPerformed();
                }
            } else if (state == State.RESUMING) {
                if (reader.available() >= verifyResumeSize) {
                    byte[] buffer = new byte[verifyResumeSize];
                    int x1 = reader.read(buffer);
                    byte[] fileBlock = new byte[verifyResumeSize];
                    int x2 = localFile.read(fileBlock);
                    if (!Arrays.equals(buffer, fileBlock)) {
                        logger.error("Tried resume, but files weren't equal");
                        gui.StatusBar.getInstance().setStatus(2, "Could not resume, files don't match", java.awt.Color.red);
                        fireDownloadFailed();
                        disconnect();
                    } else {
                        logger.debug("File resumed!");
                        if (bytesReceived == fileLength) {
                            fireDownloadComplete();
                            closeFile();
                            setState(State.COMMAND_DOWNLOAD);
                        } else {
                            setState(State.DOWNLOADING);
                        }
                    }
                    setActionPerformed();
                }
            } else if (state == State.DOWNLOADING) {
                int size = (int) Math.min(reader.available(), fileLength - bytesReceived);
                if (size > 0) {
                    byte[] buffer = new byte[size];
                    reader.read(buffer);
                    localFile.write(buffer);
                    bytesReceived += size;
                    long time = System.currentTimeMillis();
                    if (time - lastStateChange > 5000) {
                        bytesPerSecond = (int) ((bytesReceived - lastBytesReceived) * 1000 / (time - lastStateChange));
                        lastBytesReceived = bytesReceived;
                        lastStateChange = time;
                        double duration = time - startTime;
                        double averageSpeed = ((double) (bytesReceived - resumeFrom)) / duration + 0.000000000001;
                        double timeLeft = ((double) (fileLength - bytesReceived)) / averageSpeed;
                        eta = ByteConverter.getTimeString((long) timeLeft);
                    }
                    info = (bytesReceived * 100 / fileLength) + "%  " + ByteConverter.byteToShortString(bytesPerSecond) + "/s  " + ByteConverter.byteToShortString(bytesReceived) + " / " + ByteConverter.byteToShortString(fileLength) + "  (" + eta + ")";
                    if (bytesReceived == fileLength) {
                        fireDownloadComplete();
                        closeFile();
                        setState(State.COMMAND_DOWNLOAD);
                    }
                    setActionPerformed();
                    fireStateChanged();
                }
            } else if (state == State.UPLOADING) {
                if (!UserManager.getInstance().isUserBlocked(client.getNick())) {
                    String cmd = reader.readToken();
                    if (cmd != null) {
                        logger.debug("Processing command: " + cmd);
                        processCommand(cmd);
                        setActionPerformed();
                    }
                    int size = (int) Math.min(UPLOAD_BLOCK_SIZE, fileLength - bytesReceived);
                    logger.debug("File length was:" + fileLength);
                    logger.debug("Bytes recieveD:" + bytesReceived);
                    logger.debug("Size is:" + size);
                    if (size > 0) {
                        logger.debug("Loading buffer with:" + size + "bytes");
                        byte[] buffer = new byte[size];
                        int readBytes = localFile.read(buffer);
                        if (readBytes > -1) {
                            socket.getOutputStream().write(buffer, 0, readBytes);
                            socket.getOutputStream().flush();
                            bytesReceived += readBytes;
                        } else {
                            logger.debug("Read beyound file size!!");
                            socket.getOutputStream().flush();
                            setActionPerformed();
                            logger.debug("Transfer done, cleaning up!2");
                            closeFile();
                            Settings.getInstance().releaseUploadSlot();
                            setState(State.COMMAND_UPLOAD);
                            disconnect();
                        }
                        logger.debug("Actually read:" + readBytes);
                        long time = System.currentTimeMillis();
                        if (time - lastStateChange > 5000) {
                            bytesPerSecond = (int) ((bytesReceived - lastBytesReceived) * 1000 / (time - lastStateChange));
                            lastBytesReceived = bytesReceived;
                            lastStateChange = time;
                        }
                        info = (bytesReceived * 100 / fileLength) + "%  " + ByteConverter.byteToShortString(bytesPerSecond) + "/s  " + ByteConverter.byteToShortString(bytesReceived) + " / " + ByteConverter.byteToShortString(fileLength);
                        fireStateChanged();
                        setActionPerformed();
                    }
                    if (bytesReceived >= fileLength) {
                        socket.getOutputStream().flush();
                        setActionPerformed();
                        logger.debug("Transfer done, cleaning up!");
                        closeFile();
                        Settings.getInstance().releaseUploadSlot();
                        setState(State.COMMAND_UPLOAD);
                    } else {
                        sendMaxedOut();
                        setState(State.WAITING);
                    }
                }
                sendMaxedOut();
                setState(State.WAITING);
            }
        } catch (Exception e) {
            System.out.println("error running2 " + e);
            disconnect();
        }
        checkTimedOut();
    }
}
