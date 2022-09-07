class BackupThread extends Thread {
    public ChannelManager getChannelManager() {
        return ContextResolver.getChannelManager();
    }
}
