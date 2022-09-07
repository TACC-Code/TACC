class BackupThread extends Thread {
    protected ReadWriteLockBase() {
        readWriteLock = createReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }
}
