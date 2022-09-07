class BackupThread extends Thread {
    public int read() throws java.io.IOException {
        if (0 == available()) {
            requestByte(true);
        }
        synchronized (writeIn) {
            return writeIn.read();
        }
    }
}
