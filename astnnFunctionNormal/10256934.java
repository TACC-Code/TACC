class BackupThread extends Thread {
    private boolean getFileAccess() {
        return (writeLocalFileAccess() && readRemoteFileAccess());
    }
}
