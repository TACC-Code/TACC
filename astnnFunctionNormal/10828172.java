class BackupThread extends Thread {
    public static void copy(InputStream src, File dst) throws IOException {
        if (src == null) throw new NullPointerException("Source should not be NULL.");
        if (dst == null) throw new NullPointerException("Dest should not be NULL.");
        OutputStream out = new FileOutputStream(dst);
        while (src.available() != 0) {
            out.write(src.read());
        }
        out.close();
    }
}
