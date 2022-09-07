class BackupThread extends Thread {
    public static Properties loadProperties(final URL url) throws IOException {
        Properties newprops = new Properties();
        InputStream in = null;
        try {
            in = url.openStream();
            newprops.load(in);
        } finally {
            close(in);
        }
        return newprops;
    }
}
