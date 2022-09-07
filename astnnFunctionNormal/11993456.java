class BackupThread extends Thread {
    @Override
    public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
        checkWriteAccess();
        return inner.transferFrom(src, position, count);
    }
}
