class BackupThread extends Thread {
    public Thread getWriteLockedThread() {
        return writeLockedThread;
    }
}
