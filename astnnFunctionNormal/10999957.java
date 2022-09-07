class BackupThread extends Thread {
    @Override
    ChannelManager getChannelManager() {
        if (this.fContextProxy != null) {
            return (ChannelManager) this.fContextProxy.getChannelManager();
        }
        if (this.channelManager == null) {
            throw new ManagerNotFoundException("this application is running without a ChannelManager");
        }
        return this.channelManager;
    }
}
