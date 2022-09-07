class BackupThread extends Thread {
    public synchronized void endWrite() {
        if (_numActiveWriters != 1) {
            throw new IllegalStateException("Trying to end a write with " + _numActiveWriters + " active writers!");
        }
        _numActiveWriters--;
        _ensureAlreadyRunning();
        _runningThreads.remove(Thread.currentThread());
        assert _numActiveWriters == 0 && _numActiveReaders == 0 : "Multiple readers/writers were active during a write";
        _wakeFrontGroupOfWaitQueue();
    }
}
