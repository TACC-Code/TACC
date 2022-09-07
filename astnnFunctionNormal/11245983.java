class BackupThread extends Thread {
    public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
        throw nonWritableChannelException();
    }
}
