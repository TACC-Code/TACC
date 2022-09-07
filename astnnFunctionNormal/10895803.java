class BackupThread extends Thread {
    private static Properties loadCustomProperties(URL url, Properties defaults) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("loadCustomProperties(URL, Properties) - start");
        }
        Properties properties = defaults != null ? new Properties(defaults) : new Properties();
        if (url != null && url.toURI().toString().endsWith(".xml")) {
            properties.loadFromXML(url.openStream());
        } else if (url != null && !url.toURI().toString().endsWith(".xml")) {
            properties.load(url.openStream());
        }
        if (log.isDebugEnabled()) {
            log.debug("loadCustomProperties(URL, Properties) - end");
        }
        return properties;
    }
}
