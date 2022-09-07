class BackupThread extends Thread {
    private boolean tryUpload() {
        try {
            fileTransferRandomAccessFile = new RandomAccessFile(fileTransferFile, "r");
            fileTransferRandomAccessFile.getChannel().lock();
        } catch (Exception e) {
        }
        if (verifyUpload()) {
            return (setUploadStreams() && uploadFileData());
        }
        return false;
    }
}
