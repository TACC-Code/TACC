class BackupThread extends Thread {
        protected boolean getWritingLock() {
            synchronized (lock) {
                long time = System.currentTimeMillis();
                while (Thread.currentThread() != writerThread && ((System.currentTimeMillis() - time) < (1000l * getTimeout()))) {
                    if (writerThread == null || !writerThread.isAlive()) {
                        writerThread = Thread.currentThread();
                    } else {
                        try {
                            lock.wait(1000l * getTimeout());
                        } catch (InterruptedException e) {
                        }
                    }
                }
                return Thread.currentThread() == writerThread;
            }
        }
}
