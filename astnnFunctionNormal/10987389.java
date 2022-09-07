class BackupThread extends Thread {
    public static void streamIn(DataInputStream in, OutputStream dest, long maxLength) throws MaxLengthExceededException, IOException {
        long length = in.readLong();
        if (maxLength > 0 && length > maxLength) {
            throw new MaxLengthExceededException("max length (" + maxLength + ") " + "exceeded, requested length: " + length);
        }
        byte[] buf = new byte[STREAM_BUF_SIZE];
        long offset = 0;
        while (true) {
            long left = maxLength - offset;
            int readLength;
            if (left >= buf.length) {
                readLength = in.read(buf);
            } else {
                readLength = in.read(buf, 0, (int) left);
            }
            if (readLength <= 0) {
                break;
            }
            dest.write(buf, 0, readLength);
            offset += readLength;
            if (offset >= maxLength) {
                break;
            }
        }
    }
}
