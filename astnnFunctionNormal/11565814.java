class BackupThread extends Thread {
    public static void encode(InputStream ist, OutputStream os, int nOptions) throws IOException {
        for (int c = ist.read(); c != (-1); c = ist.read()) write((char) c, os, nOptions);
    }
}
