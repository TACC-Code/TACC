class BackupThread extends Thread {
        public void reset() {
            readPos = writePos = 0;
        }
}
