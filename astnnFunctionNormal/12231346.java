class BackupThread extends Thread {
    public static void copy(final InputStream pInputStream, final OutputStream pOutputStream, final long pByteLimit) throws IOException {
        if (pByteLimit < 0) {
            final byte[] buf = new byte[IO_BUFFER_SIZE];
            int read;
            while ((read = pInputStream.read(buf)) != -1) {
                pOutputStream.write(buf, 0, read);
            }
        } else {
            final byte[] buf = new byte[IO_BUFFER_SIZE];
            final int bufferReadLimit = Math.min((int) pByteLimit, IO_BUFFER_SIZE);
            long pBytesLeftToRead = pByteLimit;
            int read;
            while ((read = pInputStream.read(buf, 0, bufferReadLimit)) != -1) {
                if (pBytesLeftToRead > read) {
                    pOutputStream.write(buf, 0, read);
                    pBytesLeftToRead -= read;
                } else {
                    pOutputStream.write(buf, 0, (int) pBytesLeftToRead);
                    break;
                }
            }
        }
        pOutputStream.flush();
    }
}
