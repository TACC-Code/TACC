class BackupThread extends Thread {
    public void autoConfigChannels() {
        if (defaultChannels != null) {
            reloadChannels();
            List<NodeChannel> channels = findNodeChannels(false);
            for (Channel defaultChannel : defaultChannels) {
                if (!defaultChannel.isInList(channels)) {
                    log.info("Auto-configuring %s channel.", defaultChannel.getChannelId());
                    saveChannel(defaultChannel, true);
                } else {
                    log.info("No need to create channel %s.  It already exists.", defaultChannel.getChannelId());
                }
            }
            reloadChannels();
        }
    }
}
