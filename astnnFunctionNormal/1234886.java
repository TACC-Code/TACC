class BackupThread extends Thread {
    public void unlock(Object key) throws CacheException {
        throw new UnsupportedOperationException("SwarmCache does not support locking (use nonstrict-read-write)");
    }
}
