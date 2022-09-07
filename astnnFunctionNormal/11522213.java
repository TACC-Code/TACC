class BackupThread extends Thread {
    public void aquireWriteLock() {
        readWriteLock.writeLock().lock();
    }
}
