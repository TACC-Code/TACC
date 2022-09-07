class BackupThread extends Thread {
    public List<Channel> getPlottingChannels() {
        final List<Channel> channels = new ArrayList<Channel>(3);
        for (final ChannelWrapper wrapper : CHANNEL_WRAPPERS) {
            if (wrapper != null && wrapper.isPlotting()) {
                channels.add(wrapper.getChannel());
            }
        }
        return channels;
    }
}
