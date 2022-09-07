class BackupThread extends Thread {
    private boolean getFileChecksums() {
        return (writeLocalFileChecksum() && readRemoteFileChecksum());
    }
}
