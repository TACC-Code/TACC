class BackupThread extends Thread {
    public synchronized void ensureOpen() {
        if (_channel == null) {
            final RandomAccessFile raf;
            try {
                raf = new RandomAccessFile(_filepath, _readOnly ? "r" : "rw");
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("File not found: " + _filepath, e);
            }
            this._channel = raf.getChannel();
        }
    }
}
