class BackupThread extends Thread {
    public static void flush(final InputStream source, final OutputStream dest) throws IOException {
        final byte[] buf = new byte[8192];
        int read;
        while ((read = source.read(buf)) != -1) {
            dest.write(buf, 0, read);
        }
    }
}
