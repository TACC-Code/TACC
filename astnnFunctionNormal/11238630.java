class BackupThread extends Thread {
    private boolean writeLock(boolean waitIfNecessary) throws LockException {
        Thread thisThread = Thread.currentThread();
        WaitingThread waiter;
        synchronized (this) {
            if (writeLockedThread == thisThread) {
                outstandingWriteLocks++;
                return true;
            }
            if (writeLockedThread == null && grantWriteLock()) {
                writeLockedThread = thisThread;
                outstandingWriteLocks++;
                return true;
            }
            if (!waitIfNecessary) return false;
            deadlockCheck();
            if (waitingForWriteLock == null) waitingForWriteLock = new ArrayList<WaitingThread>(3);
            waiter = new WaitingThread(thisThread, thisThread, this, Lock.WRITE_LOCK);
            addWaitingWrite(waiter);
            DeadlockDetection.addResourceWaiter(thisThread, waiter);
        }
        List<WaitingThread> deadlockedThreads = null;
        LockException exceptionCaught = null;
        synchronized (thisThread) {
            if (thisThread != writeLockedThread) {
                while (thisThread != writeLockedThread && deadlockedThreads == null) {
                    if (LockOwner.DEBUG) {
                        StringBuffer buf = new StringBuffer("Waiting for write: ");
                        for (int i = 0; i < waitingForWriteLock.size(); i++) {
                            buf.append(' ');
                            buf.append((waitingForWriteLock.get(i)).getThread().getName());
                        }
                        LOG.debug(buf.toString());
                        debugReadLocks("WAIT");
                    }
                    deadlockedThreads = checkForDeadlock(thisThread);
                    if (deadlockedThreads == null) {
                        try {
                            waiter.doWait();
                        } catch (LockException e) {
                            exceptionCaught = e;
                            break;
                        }
                    }
                }
            }
            if (deadlockedThreads == null && exceptionCaught == null) outstandingWriteLocks++;
        }
        synchronized (this) {
            DeadlockDetection.clearResourceWaiter(thisThread);
            removeWaitingWrite(waiter);
        }
        if (exceptionCaught != null) throw exceptionCaught;
        if (deadlockedThreads != null) {
            for (WaitingThread wt : deadlockedThreads) {
                wt.signalDeadlock();
            }
            throw new DeadlockException();
        }
        return true;
    }
}
