class BackupThread extends Thread {
    public static long copy(final ReadableByteChannel read, final WritableByteChannel write, final boolean close) throws IOException {
        long readed = 0;
        try {
            final ByteBuffer buffer = NIOUtil.allocateBuffer();
            while ((read.read(buffer)) > 0) {
                buffer.flip();
                readed += write.write(buffer);
                buffer.clear();
            }
        } finally {
            if (close) {
                try {
                    read.close();
                } catch (final Exception e) {
                }
                try {
                    write.close();
                } catch (final Exception e) {
                }
            }
        }
        return readed;
    }
}
