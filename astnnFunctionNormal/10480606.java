class BackupThread extends Thread {
    public void initialize(PluginInterface _plugin_interface) {
        plugin_interface = _plugin_interface;
        log = plugin_interface.getLogger().getChannel("DLRemRules");
        BasicPluginConfigModel config = plugin_interface.getUIManager().createBasicPluginConfigModel("torrents", "download.removerules.name");
        config.addLabelParameter2("download.removerules.unauthorised.info");
        remove_unauthorised = config.addBooleanParameter2("download.removerules.unauthorised", "download.removerules.unauthorised", false);
        remove_unauthorised_seeding_only = config.addBooleanParameter2("download.removerules.unauthorised.seedingonly", "download.removerules.unauthorised.seedingonly", true);
        remove_unauthorised_data = config.addBooleanParameter2("download.removerules.unauthorised.data", "download.removerules.unauthorised.data", false);
        remove_unauthorised.addEnabledOnSelection(remove_unauthorised_seeding_only);
        remove_unauthorised.addEnabledOnSelection(remove_unauthorised_data);
        remove_update_torrents = config.addBooleanParameter2("download.removerules.updatetorrents", "download.removerules.updatetorrents", true);
        new DelayedEvent("DownloadRemovalRules", INITIAL_DELAY, new AERunnable() {

            public void runSupport() {
                plugin_interface.getDownloadManager().addListener(DownloadRemoveRulesPlugin.this);
            }
        });
    }
}
