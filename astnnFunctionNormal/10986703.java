class BackupThread extends Thread {
    public SyncCollection(Collection collection, Sync readLock, Sync writeLock) {
        c_ = collection;
        rd_ = readLock;
        wr_ = writeLock;
    }
}
