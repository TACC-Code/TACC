class BackupThread extends Thread {
    private synchronized void releaseRead(int count) {
        if (!outstandingReadLocks.isEmpty()) {
            removeReadLock(count);
            if (writeLockedThread == null && grantWriteLockAfterRead()) {
                WaitingThread waiter = waitingForWriteLock.get(0);
                removeWaitingWrite(waiter);
                DeadlockDetection.clearResourceWaiter(waiter.getThread());
                writeLockedThread = waiter.getThread();
                synchronized (writeLockedThread) {
                    writeLockedThread.notifyAll();
                }
            }
            return;
        } else {
            LOG.warn("Possible lock problem: thread " + Thread.currentThread().getName() + " released a read lock it didn't hold. Either the " + "thread was interrupted or it never acquired the lock. " + "Write lock: " + (writeLockedThread != null ? writeLockedThread.getName() : "null"), new Throwable());
            if (LockOwner.DEBUG) debugReadLocks("ILLEGAL RELEASE");
        }
    }
}
