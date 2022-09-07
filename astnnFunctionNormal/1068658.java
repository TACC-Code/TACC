class BackupThread extends Thread {
        public synchronized int available() {
            return closed ? 0 : writePos - readPos;
        }
}
