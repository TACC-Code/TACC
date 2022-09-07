class BackupThread extends Thread {
        Writer() {
            writerThread = new Thread(this);
            writerThread.start();
            logger.info("Writer started on :" + socket.getLocalSocketAddress());
        }
}
