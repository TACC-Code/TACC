class BackupThread extends Thread {
    public void initWriter() {
        int outQueueThreads = 1;
        workers = new Thread[outQueueThreads];
        for (int i = 0; i < outQueueThreads; i++) {
            workers[i] = new Thread(this, "eventwriter_" + i);
            workers[i].setDaemon(true);
            workers[i].start();
            FileLogger.info("EventWriter threads init, Create thread: " + workers[i]);
        }
    }
}
