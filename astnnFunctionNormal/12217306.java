class BackupThread extends Thread {
    public final long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
        openCheck();
        if (!src.isOpen()) {
            throw new ClosedChannelException();
        }
        throw new NonWritableChannelException();
    }
}
