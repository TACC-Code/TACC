class BackupThread extends Thread {
    public static final int decode(java.io.InputStream in, java.io.OutputStream out) throws java.io.IOException {
        Decoder dec = new Decoder(in);
        int read, buflen = 512, acc = 0;
        byte[] buf = new byte[buflen];
        while (0 < (read = dec.read(buf, 0, buflen))) {
            acc += read;
            out.write(buf, 0, read);
        }
        return acc;
    }
}
