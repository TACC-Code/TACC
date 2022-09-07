class BackupThread extends Thread {
    public static InputStream getResourceStream(String path) throws java.io.IOException {
        URL url;
        if ((url = getResourceURL(path)) == null) {
            return null;
        }
        return url.openStream();
    }
}
