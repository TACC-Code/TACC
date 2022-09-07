class BackupThread extends Thread {
    public List<Channel> getMonitoredChannels() {
        final List<Channel> channels = new ArrayList<Channel>(3);
        for (final ChannelWrapper wrapper : CHANNEL_WRAPPERS) {
            if (wrapper != null && wrapper.isEnabled()) {
                channels.add(wrapper.getChannel());
            }
        }
        return channels;
    }
}
