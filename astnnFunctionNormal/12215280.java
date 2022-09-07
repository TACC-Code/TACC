class BackupThread extends Thread {
    public Map<K, V> asMap() {
        try {
            readWriteLock.writeLock().lock();
            return new RevisionObjectCacheMap<K, V>(this);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
