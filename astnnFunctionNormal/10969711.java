class BackupThread extends Thread {
    public static final InputStream getInputStreamForURL(final String path) throws ConfigurationException {
        final URL url = ResourceLocator.locateURL(path);
        if (url == null) {
            String msg = "Unable to locate config file: " + path;
            log.log(Level.SEVERE, msg);
            throw new ConfigurationException(msg);
        }
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new ConfigurationException("Unable to open config file: " + path, e);
        }
    }
}
