class BackupThread extends Thread {
    public void start() {
        if (readerThread != null) {
            throw new IllegalStateException("Network node already started.");
        }
        readerThread = new Thread(new Runnable() {

            public void run() {
                read();
            }
        }, nodeId + "(reader)");
        readerThread.start();
        writerTread = new Thread(new Runnable() {

            public void run() {
                write();
            }
        }, nodeId + "(writer)");
        writerTread.start();
    }
}
