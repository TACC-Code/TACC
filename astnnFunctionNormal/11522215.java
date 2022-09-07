class BackupThread extends Thread {
    public void releaseWriteLock() {
        readWriteLock.writeLock().unlock();
    }
}
