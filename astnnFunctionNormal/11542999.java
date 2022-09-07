class BackupThread extends Thread {
    public synchronized void endRead() {
        if (_numActiveReaders == 0) {
            throw new IllegalStateException("Trying to end a read with no active readers!");
        }
        _numActiveReaders--;
        _ensureAlreadyRunning();
        _runningThreads.remove(Thread.currentThread());
        assert _numActiveWriters == 0 : "A writer was active during the read";
        if (_numActiveReaders == 0) {
            _wakeFrontGroupOfWaitQueue();
        }
    }
}
