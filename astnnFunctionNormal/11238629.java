class BackupThread extends Thread {
    private synchronized boolean readLock(boolean waitIfNecessary) throws LockException {
        final Thread thisThread = Thread.currentThread();
        if (writeLockedThread == thisThread) {
            outstandingReadLocks.add(new LockOwner(thisThread));
            return true;
        }
        deadlockCheck();
        waitingForReadLock++;
        if (writeLockedThread != null) {
            if (!waitIfNecessary) return false;
            WaitingThread waiter = new WaitingThread(thisThread, this, this, Lock.READ_LOCK);
            DeadlockDetection.addResourceWaiter(thisThread, waiter);
            while (writeLockedThread != null) {
                waiter.doWait();
            }
            DeadlockDetection.clearResourceWaiter(thisThread);
        }
        waitingForReadLock--;
        outstandingReadLocks.add(new LockOwner(thisThread));
        return true;
    }
}
