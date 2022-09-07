class BackupThread extends Thread {
    void start() throws IOException {
        try {
            systemThreadPool.execute(new Writer(), "mux writer");
            systemThreadPool.execute(new Reader(), "mux reader");
        } catch (OutOfMemoryError e) {
            try {
                logger.log(Level.WARNING, "could not create thread for request dispatch", e);
            } catch (Throwable t) {
            }
            IOException ioe = new IOException("could not create I/O threads");
            ioe.initCause(e);
            throw ioe;
        }
    }
}
