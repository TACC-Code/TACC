class BackupThread extends Thread {
        public synchronized void addData(byte[] b) {
            while (writePos + b.length > buffer.length) {
                byte[] b2 = new byte[buffer.length * 2];
                System.arraycopy(buffer, readPos, b2, 0, writePos - readPos);
                buffer = b2;
                writePos -= readPos;
                readPos = 0;
            }
            System.arraycopy(b, 0, buffer, writePos, b.length);
            writePos += b.length;
            this.notify();
        }
}
