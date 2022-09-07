class BackupThread extends Thread {
    public static void copyStream(InputStream in, OutputStream out) throws IllegalArgumentException, IOException {
        if (in == null) {
            throw new IllegalArgumentException("in == null");
        }
        byte[] buf = makeBuf();
        try {
            while (true) {
                int read = in.read(buf);
                if (read < 0) {
                    return;
                }
                out.write(buf, 0, read);
            }
        } finally {
            releaseBuf(buf);
        }
    }
}
