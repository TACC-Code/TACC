class BackupThread extends Thread {
    protected void autoConfigChannels() {
        if (defaultChannels != null) {
            reloadChannels();
            List<NodeChannel> channels = getNodeChannels(false);
            for (Channel defaultChannel : defaultChannels) {
                if (!defaultChannel.isInList(channels)) {
                    log.info("Auto-configuring {} channel.", defaultChannel.getChannelId());
                    saveChannel(defaultChannel, true);
                } else {
                    log.debug("No need to create channel {}.  It already exists", defaultChannel.getChannelId());
                }
            }
            reloadChannels();
        }
    }
}
