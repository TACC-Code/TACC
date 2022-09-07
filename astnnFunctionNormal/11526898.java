class BackupThread extends Thread {
    public void release() throws ReplicatorException, InterruptedException {
        connectionManager.releaseAll();
        if (!readOnly) writeLock.release();
        stopLogSyncTask();
    }
}
