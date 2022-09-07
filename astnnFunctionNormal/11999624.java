class BackupThread extends Thread {
    public Lock lockItem(String itemId, boolean writeLock) {
        synchronized (itemLockMap) {
            if (repositoryLock == null) {
                throw new RuntimeException("Repository closed.");
            }
            LockMapEntry entry = getLockMapEntry(itemId, true);
            entry.checkLocks();
            Lock lock = new DefaultLock(itemId, lockIdSalt, nextLockId++);
            if (writeLock) {
                if (entry.isLocked()) {
                    throw new RepositoryLockException("Failed to obtain write lock; item already locked.");
                }
                entry.lockForWriting(lock);
            } else {
                if (entry.isWriteLocked()) {
                    throw new RepositoryLockException("Failed to obtain read lock; item is write locked.");
                }
                entry.lockForReading(lock);
            }
            return lock;
        }
    }
}
