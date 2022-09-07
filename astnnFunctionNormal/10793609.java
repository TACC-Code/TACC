class BackupThread extends Thread {
    public static InputStream getUncachedStream(final URL url) throws IOException {
        if (url != null) {
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            return conn.getInputStream();
        } else {
            return null;
        }
    }
}
