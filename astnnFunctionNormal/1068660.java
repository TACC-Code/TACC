class BackupThread extends Thread {
        private synchronized void waitForData() throws IOException {
            while (writePos <= readPos) {
                if (closed == true) throw new IOException("Connection closed");
                try {
                    this.wait(50);
                } catch (Exception e) {
                }
            }
        }
}
