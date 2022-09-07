class BackupThread extends Thread {
    public boolean isOpen() {
        return readSink.isOpen() && writeSink.isOpen();
    }
}
