class BackupThread extends Thread {
    public ThreadedInputStream(final InputStream is) throws RippleException {
        source = is;
        eager = false;
        readerTask = null;
        try {
            writeIn = new PipedInputStream();
            readOut = new PipedOutputStream(writeIn);
        } catch (java.io.IOException e) {
            throw new RippleException(e);
        }
    }
}
