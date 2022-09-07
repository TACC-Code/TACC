class BackupThread extends Thread {
    public int getChannelCount() {
        if (channelAwareComponent == null) {
            return 0;
        }
        List<MessageAlertChannel> channels = channelAwareComponent.getChannels();
        if (CollectionUtils.isBlankCollection(channels)) {
            return 0;
        }
        return channels.size();
    }
}
