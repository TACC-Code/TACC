class BackupThread extends Thread {
    public void lockForReading() {
        try {
            for (int i = 0; i < MAX_RETRIES; i++) {
                if (startRead()) {
                    return;
                }
                Thread.sleep(RETRY_TIME);
            }
            throw new IllegalStateException("Can't get a read lock. Tried for " + RETRY_TIME * MAX_RETRIES + "ms. The mutex was locked for write " + (System.currentTimeMillis() - writeStartTime) + "ms ago by the following party", writeStartTrace);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            logger.severe(ex.getMessage(), ex);
        }
    }
}
