class BackupThread extends Thread {
        protected Fragment(int fragmentCapacity) {
            this.readWriteLock = new ReentrantReadWriteLock();
            this.readLock = this.readWriteLock.readLock();
            this.purgeLock = this.readWriteLock.writeLock();
            this.readers = new AtomicInteger(0);
            this.capacity = fragmentCapacity;
            this.data = new Object[fragmentCapacity][];
            this.fillIndex = new AtomicInteger(0);
        }
}
