class BackupThread extends Thread {
    public void initialize(PluginInterface _plugin_interface) {
        plugin_interface = _plugin_interface;
        plugin_interface.getPluginProperties().setProperty("plugin.name", "Core Updater");
        log = plugin_interface.getLogger().getChannel("CoreUpdater");
        rd_logger = new ResourceDownloaderAdapter() {

            public void reportActivity(ResourceDownloader downloader, String activity) {
                log.log(activity);
            }
        };
        Properties props = plugin_interface.getPluginProperties();
        props.setProperty("plugin.version", plugin_interface.getAzureusVersion());
        rdf = plugin_interface.getUtilities().getResourceDownloaderFactory();
        plugin_interface.getUpdateManager().registerUpdatableComponent(this, true);
    }
}
