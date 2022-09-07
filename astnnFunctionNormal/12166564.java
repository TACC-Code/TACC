class BackupThread extends Thread {
    public static void CopyStream(InputStream input, OutputStream output, int bufferSize, long totalLength) throws IOException {
        long position = 0;
        byte[] buffer = new byte[bufferSize];
        do {
            int CurrentReadSize = bufferSize;
            if ((totalLength - position) < bufferSize) {
                CurrentReadSize = (int) (totalLength - position);
            }
            int readCount = input.read(buffer, 0, CurrentReadSize);
            output.write(buffer, 0, readCount);
            position += readCount;
        } while (position < totalLength);
    }
}
