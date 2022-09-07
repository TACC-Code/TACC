class BackupThread extends Thread {
    private boolean isNewestVersion() {
        try {
            ServiceReference serviceReference = bundleContext.getServiceReference(net.java.sip.communicator.service.version.VersionService.class.getName());
            VersionService verService = (VersionService) bundleContext.getService(serviceReference);
            net.java.sip.communicator.service.version.Version ver = verService.getCurrentVersion();
            String configString = Resources.getConfigString("update_link");
            if (configString == null) {
                logger.debug("Updates are disabled. Faking latest version.");
                return true;
            }
            URL url = new URL(configString);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            Properties props = new Properties();
            props.load(conn.getInputStream());
            lastVersion = props.getProperty("last_version");
            downloadLink = props.getProperty("download_link");
            changesLink = configString.substring(0, configString.lastIndexOf("/") + 1) + props.getProperty("changes_html");
            return lastVersion.compareTo(ver.toString()) <= 0;
        } catch (Exception e) {
            logger.warn("Cannot get and compare versions!");
            logger.debug("Error was: ", e);
            return true;
        }
    }
}
