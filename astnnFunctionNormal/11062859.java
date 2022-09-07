class BackupThread extends Thread {
    public ConcurrentEventSinkRegistry() {
        super();
        final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        m_channelEventSinkAffinityReadLock = readWriteLock.readLock();
        m_channelEventSinkAffinityWriteLock = readWriteLock.writeLock();
    }
}
