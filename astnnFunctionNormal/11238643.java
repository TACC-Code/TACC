class BackupThread extends Thread {
    private List<WaitingThread> checkForDeadlock(Thread waiter) {
        ArrayList<WaitingThread> waiters = new ArrayList<WaitingThread>(10);
        if (DeadlockDetection.wouldDeadlock(waiter, writeLockedThread, waiters)) {
            LOG.warn("Potential deadlock detected on lock " + getId() + "; killing threads: " + waiters.size());
            return waiters.size() > 0 ? waiters : null;
        }
        return null;
    }
}
