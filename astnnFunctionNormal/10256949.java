class BackupThread extends Thread {
    private boolean tryDownload() {
        try {
            fileTransferRandomAccessFile = new RandomAccessFile(fileTransferFile, "rw");
            fileTransferRandomAccessFile.getChannel().lock();
        } catch (Exception e) {
        }
        if (verifyDownload()) {
            return (setDownloadStreams() && downloadFileData());
        } else {
            return false;
        }
    }
}
