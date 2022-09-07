class BackupThread extends Thread {
    private boolean getFileSizes() {
        return (writeLocalFileSize() && readRemoteFileSize());
    }
}
