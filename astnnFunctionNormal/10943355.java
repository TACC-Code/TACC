class BackupThread extends Thread {
    public static final InputStream getConfigStream(final String path) throws JHomenetException {
        final URL url = ConfigHelper.locateConfig(path);
        if (url == null) {
            String msg = "Unable to locate config file: " + path;
            log.fatal(msg);
            throw new JHomenetException(msg);
        }
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new JHomenetException("Unable to open config file: " + path, e);
        }
    }
}
