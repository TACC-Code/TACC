class BackupThread extends Thread {
    public boolean isWritable() {
        return (!readOnly && writeLock.isLocked());
    }
}
