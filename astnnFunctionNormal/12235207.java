class BackupThread extends Thread {
    public synchronized void flush(boolean close) throws IOException {
        if (!open) {
            throw new IllegalStateException("not opened: " + file.getAbsolutePath());
        }
        directory.flush(close);
        syncRecordLength();
        raf.getChannel().force(true);
        if (close) {
            close();
        }
    }
}
