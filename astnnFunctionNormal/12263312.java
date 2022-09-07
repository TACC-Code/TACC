class BackupThread extends Thread {
    private void detectAndInitPlugins(String propertyFile, Properties filterConfiguration, String type) throws Exception {
        Enumeration<URL> configurationURLs;
        configurationURLs = StatelessFilter.class.getClassLoader().getResources(propertyFile);
        URL url = null;
        Properties pluginConfiguration = null;
        InputStream is = null;
        while (configurationURLs.hasMoreElements()) {
            url = configurationURLs.nextElement();
            if (logger.isDebugEnabled()) {
                logger.debug(DEBUG_PROCESSING + url.toString());
            }
            pluginConfiguration = new Properties();
            is = url.openStream();
            pluginConfiguration.load(is);
            is.close();
            initPlugin(pluginConfiguration, filterConfiguration, type);
        }
    }
}
