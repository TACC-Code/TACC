class BackupThread extends Thread {
    public void close(RevisionObjectCache<K, V> cache) {
        try {
            readWriteLock.writeLock().lock();
            this.openRevisionCaches.remove(cache);
            expire();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
