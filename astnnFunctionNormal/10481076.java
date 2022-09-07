class BackupThread extends Thread {
        public void stop() {
            running = false;
            writerThread.interrupt();
        }
}
