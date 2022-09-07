class BackupThread extends Thread {
    protected void transferInThread(RvConnection rvtransfer) throws IOException, FailureEventException {
        RvSessionConnectionInfo conninfo = rvtransfer.getRvSessionInfo();
        IncomingFileTransfer itransfer = (IncomingFileTransfer) rvtransfer;
        if (plumber == null) {
            plumber = new IncomingFileTransferPlumberImpl(itransfer, this);
        }
        List<TransferredFile> files = new ArrayList<TransferredFile>();
        EventPost eventpost = itransfer.getEventPost();
        long icbmId = conninfo.getRvSession().getRvSessionId();
        boolean finished = false;
        boolean stop = false;
        while (!stop && !shouldStop()) {
            LOGGER.fine("Waiting for next FT packet");
            FileTransferHeader sendheader = plumber.readHeader();
            if (sendheader == null) {
                LOGGER.info("Couldn't read FT header");
                break;
            }
            assert sendheader.getHeaderType() == HEADERTYPE_SENDHEADER;
            long desiredChecksum = sendheader.getChecksum();
            if (rvtransfer.getRvSessionInfo().getInitiator() == Initiator.ME) {
                long sentid = sendheader.getIcbmMessageId();
                if (sentid != icbmId) {
                    LOGGER.info("Other end sent " + sentid + " but we're looking for " + icbmId);
                    break;
                }
            }
            setConnected();
            SegmentedFilename segName = sendheader.getFilename();
            TransferredFile destFile = plumber.getNativeFile(segName, sendheader.getMacFileInfo());
            files.add(destFile);
            boolean attemptResume = plumber.shouldAttemptResume(destFile);
            FileChannel fileChannel = destFile.getChannel();
            long toDownload;
            if (attemptResume) {
                FileTransferHeader outHeader = new FileTransferHeader(sendheader);
                outHeader.setHeaderType(HEADERTYPE_RESUME);
                outHeader.setIcbmMessageId(icbmId);
                long len = destFile.getSize();
                outHeader.setBytesReceived(len);
                Checksummer summer = plumber.getChecksummer(destFile, len);
                eventpost.fireEvent(new ChecksummingEvent(destFile, summer));
                outHeader.setReceivedChecksum(summer.compute());
                outHeader.setCompression(0);
                outHeader.setEncryption(0);
                plumber.sendHeader(outHeader);
                FileTransferHeader resumeResponse = plumber.readHeader();
                if (resumeResponse == null) {
                    LOGGER.info("Didn't receive resume response; connection closed");
                    break;
                }
                assert resumeResponse.getHeaderType() == HEADERTYPE_RESUME_SENDHEADER : resumeResponse.getHeaderType();
                long bytesReceived = resumeResponse.getBytesReceived();
                assert bytesReceived <= len : "sender is trying to trick us: " + bytesReceived + " > " + len;
                if (bytesReceived != len) {
                    eventpost.fireEvent(new ResumeChecksumFailedEvent(destFile));
                }
                fileChannel.position(bytesReceived);
                fileChannel.truncate(bytesReceived);
                toDownload = resumeResponse.getFileSize() - bytesReceived;
                FileTransferHeader finalResponse = new FileTransferHeader(resumeResponse);
                finalResponse.setHeaderType(HEADERTYPE_RESUME_ACK);
                plumber.sendHeader(finalResponse);
            } else {
                FileTransferHeader outHeader = new FileTransferHeader(sendheader);
                outHeader.setIcbmMessageId(icbmId);
                fileChannel.truncate(0);
                outHeader.setHeaderType(HEADERTYPE_ACK);
                outHeader.setBytesReceived(0);
                outHeader.setReceivedChecksum(0);
                outHeader.setCompression(0);
                outHeader.setEncryption(0);
                outHeader.setFlags(0);
                plumber.sendHeader(outHeader);
                toDownload = sendheader.getFileSize();
            }
            long startedAt = fileChannel.position();
            Transferrer receiver = plumber.createTransferrer(destFile, startedAt, toDownload);
            TransferringFileInfo info = new TransferringFileInfo(destFile, startedAt, startedAt + toDownload);
            eventpost.fireEvent(new TransferringFileEvent(info, receiver));
            long downloaded = receiver.transfer();
            if (downloaded != toDownload) {
                LOGGER.fine("Didn't download correct number of bytes: downloaded " + downloaded + ", wanted " + toDownload);
                break;
            }
            long sumLength = startedAt + downloaded;
            Checksummer summer = plumber.getChecksummer(destFile, sumLength);
            eventpost.fireEvent(new ChecksummingEvent(destFile, summer));
            long calculatedSum = summer.compute();
            destFile.close();
            boolean failed = calculatedSum != desiredChecksum;
            if (!failed) eventpost.fireEvent(new FileCompleteEvent(info));
            FileTransferHeader doneHeader;
            try {
                doneHeader = new FileTransferHeader(sendheader);
                doneHeader.setHeaderType(FileTransferHeader.HEADERTYPE_RECEIVED);
                doneHeader.setFlags(doneHeader.getFlags() | FileTransferHeader.FLAG_DONE);
                doneHeader.setIcbmMessageId(icbmId);
                doneHeader.setFilesLeft(doneHeader.getFilesLeft() - 1);
                if (doneHeader.getFilesLeft() == 0) {
                    doneHeader.setPartsLeft(doneHeader.getPartsLeft() - 1);
                }
                doneHeader.setBytesReceived(startedAt + downloaded);
                doneHeader.setReceivedChecksum(calculatedSum);
                plumber.sendHeader(doneHeader);
            } finally {
                if (failed) {
                    fireFailed(new CorruptTransferEvent(info));
                    stop = true;
                }
            }
            int filesLeft = doneHeader.getFilesLeft();
            int partsLeft = doneHeader.getPartsLeft();
            if (filesLeft == 0 && partsLeft == 0) {
                finished = true;
                break;
            } else {
                LOGGER.info("Waiting for " + filesLeft + " files and " + partsLeft + " parts");
            }
        }
        if (finished) {
            fireSucceeded(new TransferSucceededInfo(files));
        } else {
            fireFailed(new UnknownErrorEvent());
        }
    }
}
