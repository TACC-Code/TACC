class BackupThread extends Thread {
    private void _ensureNotAlreadyRunning() {
        if (_runningThreads.contains(Thread.currentThread())) {
            throw new IllegalStateException("Same thread cannot read or write multiple times!  (Would cause deadlock.)");
        }
    }
}
