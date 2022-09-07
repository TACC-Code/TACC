class BackupThread extends Thread {
    public int getMessageCount() {
        List<MessageAlertChannel> channels = channelAwareComponent.getChannels();
        if (CollectionUtils.isBlankCollection(channels)) {
            return 0;
        }
        return channels.size();
    }
}
