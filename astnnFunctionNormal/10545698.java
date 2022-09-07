class BackupThread extends Thread {
    public static InputStream getInputStreamForURL(URL url) throws IOException {
        JarURLConnection conn = (JarURLConnection) url.openConnection();
        return conn.getInputStream();
    }
}
