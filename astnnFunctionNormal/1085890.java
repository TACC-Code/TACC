class BackupThread extends Thread {
    public static long copy(final ReadableByteChannel read, final WritableByteChannel write) throws IOException {
        return NIOUtil.copy(read, write, true);
    }
}
