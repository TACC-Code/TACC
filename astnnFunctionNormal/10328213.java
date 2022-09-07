class BackupThread extends Thread {
    public static InputStream getInputStream(URL url) throws IOException {
        InputStream in = url.openStream();
        return in;
    }
}
