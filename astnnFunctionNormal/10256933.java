class BackupThread extends Thread {
    private boolean getFileStatus() {
        return (writeLocalFileStatus() && readRemoteFileStatus());
    }
}
