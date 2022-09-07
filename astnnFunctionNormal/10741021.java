class BackupThread extends Thread {
    private Properties readProperties() {
        Properties props = new Properties();
        try {
            Enumeration urls = new PermGenCleanerUtil().getCurrentClassLoader().getResources(PROPERTY_FILE_NAME);
            while (urls.hasMoreElements()) {
                URL url = (URL) urls.nextElement();
                logger.debug("Loading settings from property file '" + url + "'");
                InputStream is = url.openStream();
                props.load(is);
                is.close();
            }
        } catch (IOException e) {
            throw new PermGenCleanerException("Error while loading settings from property file", e);
        }
        return props;
    }
}
