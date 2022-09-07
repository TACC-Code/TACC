class BackupThread extends Thread {
    public long transferFrom(FileChannel source) throws IOException, BufferOverflowException {
        synchronized (delegate) {
            return delegate.transferFrom(source);
        }
    }
}
