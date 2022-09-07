class BackupThread extends Thread {
    public void close() {
        if (_read_write) {
            closeWrite();
        } else {
            closeRead();
        }
    }
}
