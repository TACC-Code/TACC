class BackupThread extends Thread {
    public SyncCollection(Collection collection, ReadWriteLock rwl) {
        this(collection, rwl.readLock(), rwl.writeLock());
    }
}
