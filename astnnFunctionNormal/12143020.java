class BackupThread extends Thread {
    private synchronized void stopWrite() {
        writing = false;
        writeStartTime = 0;
        writeStartTrace = null;
        if (SHOW_WRITE_LOCKING_OPS) {
            logger.info(Thread.currentThread().getName() + " returns a write lock");
        }
    }
}
