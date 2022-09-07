class BackupThread extends Thread {
    public static java.io.InputStream getFileInputStream(URL _url) {
        try {
            return _url.openStream();
        } catch (Exception err) {
            throw new RuntimeException(err);
        }
    }
}
