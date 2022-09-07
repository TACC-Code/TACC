class BackupThread extends Thread {
    private synchronized void releaseWrite(int count) {
        if (Thread.currentThread() == writeLockedThread) {
            if (outstandingWriteLocks > 0) outstandingWriteLocks -= count;
            if (outstandingWriteLocks > 0) {
                return;
            }
            if (grantWriteLockAfterRead()) {
                WaitingThread waiter = waitingForWriteLock.get(0);
                removeWaitingWrite(waiter);
                DeadlockDetection.clearResourceWaiter(waiter.getThread());
                writeLockedThread = waiter.getThread();
                synchronized (writeLockedThread) {
                    writeLockedThread.notifyAll();
                }
            } else {
                writeLockedThread = null;
                if (waitingForReadLock > 0) {
                    notifyAll();
                }
            }
        } else {
            LOG.warn("Possible lock problem: a thread released a write lock it didn't hold. Either the " + "thread was interrupted or it never acquired the lock.", new Throwable());
        }
    }
}
