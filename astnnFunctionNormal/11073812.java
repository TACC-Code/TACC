class BackupThread extends Thread {
    public void setChannelEnable(final int index, final boolean enable) {
        if (!enable && isChannelPlotting(index)) {
            setChannelPlotting(index, false);
        }
        final ChannelWrapper wrapper = getChannelWrapper(index);
        if (wrapper != null) {
            wrapper.setEnable(enable);
            final Channel channel = wrapper.getChannel();
            if (enable) {
                CORRELATOR.addChannel(channel);
            } else {
                if (CORRELATOR.hasSource(channel.getId())) {
                    CORRELATOR.removeChannel(channel);
                }
            }
        }
        final List<Channel> monitoredChannels = getMonitoredChannels();
        synchronized (MONITOR_CHANNEL_IDs) {
            MONITOR_CHANNEL_IDs.clear();
            for (final Channel channel : monitoredChannels) {
                MONITOR_CHANNEL_IDs.add(channel.getId());
            }
        }
        EVENT_PROXY.monitoredChannelsChanged(monitoredChannels);
    }
}
