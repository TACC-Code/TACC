class BackupThread extends Thread {
    public static void pump(InputStream in, OutputStream out, int bufferSize, boolean flushAfterWrite) throws IOException {
        int totalBytesRead = 0;
        final byte[] buffer = new byte[bufferSize];
        while (true) {
            final int readCount = in.read(buffer);
            if (readCount == -1) {
                break;
            }
            out.write(buffer, 0, readCount);
            totalBytesRead += readCount;
            if (flushAfterWrite) {
                out.flush();
            }
        }
    }
}
