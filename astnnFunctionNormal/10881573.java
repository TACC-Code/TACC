class BackupThread extends Thread {
    public static final int encode(java.io.InputStream in, java.io.OutputStream out) throws java.io.IOException {
        Encoder enc = new Encoder(out);
        int read, buflen = 512, acc = 0;
        byte[] buf = new byte[buflen];
        while (0 < (read = in.read(buf, 0, buflen))) {
            enc.write(buf, 0, read);
            acc += read;
        }
        enc.flush();
        return acc;
    }
}
