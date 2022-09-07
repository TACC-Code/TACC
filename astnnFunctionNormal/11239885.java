class BackupThread extends Thread {
    private synchronized void loadGlobalSettings() {
        String propFileName = System.getProperty(DozerConstants.CONFIG_FILE_SYS_PROP);
        if (MappingUtils.isBlankOrNull(propFileName)) {
            propFileName = DozerConstants.DEFAULT_CONFIG_FILE;
        }
        log.info("Trying to find Dozer configuration file: {}", propFileName);
        DozerClassLoader classLoader = BeanContainer.getInstance().getClassLoader();
        URL url = classLoader.loadResource(propFileName);
        if (url == null) {
            log.warn("Dozer configuration file not found: {}.  Using defaults for all Dozer global properties.", propFileName);
            return;
        } else {
            log.info("Using URL [{}] for Dozer global property configuration", url);
        }
        Properties props = new Properties();
        InputStream inputStream = null;
        try {
            log.info("Reading Dozer properties from URL [{}]", url);
            inputStream = url.openStream();
            props.load(inputStream);
        } catch (IOException e) {
            MappingUtils.throwMappingException("Problem loading Dozer properties from URL [" + propFileName + "]", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        populateSettings(props);
        loadedByFileName = propFileName;
        log.debug("Finished configuring Dozer global properties");
    }
}
