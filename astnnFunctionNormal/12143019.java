class BackupThread extends Thread {
    private synchronized boolean startWrite() {
        if (reader == 0 && !writing) {
            if (SHOW_WRITE_LOCKING_OPS) {
                logger.info(Thread.currentThread().getName() + " get a write lock");
            }
            writing = true;
            writeStartTime = System.currentTimeMillis();
            writeStartTrace = new Throwable();
            return true;
        } else {
            return false;
        }
    }
}
