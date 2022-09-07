class BackupThread extends Thread {
    private static Properties getDefaultProperties(URL url) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("loadDefaultProperties(URL) - url {" + url + "}");
        }
        Properties properties = new Properties();
        if (url != null && url.toURI().toString().endsWith(".properties")) {
            properties.load(url.openStream());
        } else if (url != null && url.toURI().toString().endsWith(".xml")) {
            properties.loadFromXML(url.openStream());
        }
        if (log.isDebugEnabled()) {
            log.debug("loadDefaultProperties(URL) - loaded properties");
        }
        return properties;
    }
}
