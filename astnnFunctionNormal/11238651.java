class BackupThread extends Thread {
    public synchronized LockInfo getLockInfo() {
        LockInfo info;
        String[] readers = new String[0];
        if (outstandingReadLocks != null) {
            readers = new String[outstandingReadLocks.size()];
            for (int i = 0; i < outstandingReadLocks.size(); i++) {
                LockOwner owner = outstandingReadLocks.get(i);
                readers[i] = owner.getOwner().getName();
            }
        }
        if (writeLockedThread != null) {
            info = new LockInfo(LockInfo.RESOURCE_LOCK, LockInfo.WRITE_LOCK, getId(), new String[] { writeLockedThread.getName() });
            info.setReadLocks(readers);
        } else {
            info = new LockInfo(LockInfo.RESOURCE_LOCK, LockInfo.READ_LOCK, getId(), readers);
        }
        if (waitingForWriteLock != null) {
            String waitingForWrite[] = new String[waitingForWriteLock.size()];
            for (int i = 0; i < waitingForWriteLock.size(); i++) {
                waitingForWrite[i] = waitingForWriteLock.get(i).getThread().getName();
            }
            info.setWaitingForWrite(waitingForWrite);
        }
        return info;
    }
}
