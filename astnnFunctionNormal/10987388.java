class BackupThread extends Thread {
    public static void streamOut(DataOutputStream out, InputStream source, long length) throws IOException {
        out.writeLong(length);
        byte[] buf = new byte[STREAM_BUF_SIZE];
        int readLength;
        while ((readLength = source.read(buf)) > 0) {
            out.write(buf, 0, readLength);
        }
    }
}
