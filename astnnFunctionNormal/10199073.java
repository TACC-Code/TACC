class BackupThread extends Thread {
    public ChannelPlugIn(PluginConfiguration aConfiguration) {
        super(aConfiguration);
        if (mLog.isDebugEnabled()) {
            mLog.debug("Instantiating channel plug-in...");
        }
        this.setNamespace(NS_CHANNELS_DEFAULT);
        mChannelManager = ChannelManager.getChannelManager(aConfiguration.getSettings());
    }
}
