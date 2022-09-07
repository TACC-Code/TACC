class BackupThread extends Thread {
    public static void copy(InputStream source, OutputStream dest, long amount) throws IOException {
        byte[] buf = new byte[8192];
        long copied = 0;
        while (copied < amount) {
            int toRead = 8192;
            if ((amount - copied) < 8192) {
                toRead = (int) (amount - copied);
            }
            int read = source.read(buf, 0, toRead);
            if (read == -1) {
                throw new IOException("Inputstream has to continue for another " + (amount - copied) + " bytes.");
            }
            dest.write(buf, 0, read);
            copied += read;
        }
    }
}
