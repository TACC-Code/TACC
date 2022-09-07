class BackupThread extends Thread {
    public void reset() {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            indexFile.delete();
            dataFile.delete();
        } finally {
            lock.unlock();
        }
    }
}
