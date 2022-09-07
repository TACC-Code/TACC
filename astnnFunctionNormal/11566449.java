class BackupThread extends Thread {
    public static void loadFile(final URL url, final StringBuffer buffer, final String encoding) throws IOException {
        InputStream in = null;
        BufferedReader dis = null;
        try {
            in = url.openStream();
            dis = new BufferedReader(new InputStreamReader(in, encoding));
            int i;
            while ((i = dis.read()) != -1) {
                buffer.append((char) i);
            }
        } finally {
            close(in);
            close(dis);
        }
    }
}
