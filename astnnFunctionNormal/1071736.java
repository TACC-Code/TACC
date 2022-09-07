class BackupThread extends Thread {
    @Override
    @SuppressWarnings("synthetic-access")
    public ChannelMonitor getChannelMonitor() {
        if (monitorChannels) {
            return new SimpleChannelMonitor();
        }
        return null;
    }
}
