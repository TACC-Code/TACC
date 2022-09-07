class BackupThread extends Thread {
    public void lockForWriting() {
        try {
            for (int i = 0; i < MAX_RETRIES; i++) {
                if (startWrite()) {
                    return;
                }
                Thread.sleep(RETRY_TIME);
            }
            if (writing) {
                throw new IllegalStateException("Can't get a write lock. Tried for " + RETRY_TIME * MAX_RETRIES + "ms. The mutex was locked for write " + (System.currentTimeMillis() - writeStartTime) + "ms ago by the following party", writeStartTrace);
            } else {
                throw new IllegalStateException("Can't get a write lock. Tried for " + RETRY_TIME * MAX_RETRIES + "ms. Some read lock is in the way. Open readers=" + readers);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            logger.severe(ex.getMessage(), ex);
        }
    }
}
