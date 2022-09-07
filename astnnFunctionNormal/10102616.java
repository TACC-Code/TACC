class BackupThread extends Thread {
    public InputStream getResourceAsStream(final String path) {
        final URL url = getResource(path);
        if (url == null) {
            return null;
        }
        try {
            return url.openStream();
        } catch (IOException ioe) {
            return null;
        }
    }
}
