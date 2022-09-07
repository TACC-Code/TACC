class BackupThread extends Thread {
    private void _ensureAlreadyRunning() {
        if (!_runningThreads.contains(Thread.currentThread())) {
            throw new IllegalStateException("Current thread did not initiate a read or write!");
        }
    }
}
