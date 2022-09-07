class BackupThread extends Thread {
    void changeLock(int newLockMode) {
        if (broker == null || handle == null) throw new IllegalStateException("broker not acquired");
        if (lockMode == newLockMode) return;
        if (lockMode == Lock.NO_LOCK) {
            try {
                handle.getLock().acquire(newLockMode);
                lockMode = newLockMode;
            } catch (LockException e) {
                throw new DatabaseException(e);
            }
        } else {
            if (newLockMode != Lock.NO_LOCK) throw new IllegalStateException("cannot change between read and write lock modes");
            handle.getLock().release(lockMode);
            lockMode = newLockMode;
        }
    }
}
