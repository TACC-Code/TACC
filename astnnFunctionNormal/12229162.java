class BackupThread extends Thread {
    public static void loadProperties(Properties p, URL url) throws IOException {
        InputStream propIn = url.openStream();
        try {
            p.load(propIn);
        } finally {
            closeStream(propIn);
        }
    }
}
