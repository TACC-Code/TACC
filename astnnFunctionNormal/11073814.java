class BackupThread extends Thread {
    public void setChannelPlotting(final int index, final boolean plot) {
        if (plot && !isChannelEnabled(index)) {
            setChannelEnable(index, true);
        }
        final ChannelWrapper wrapper = getChannelWrapper(index);
        if (wrapper != null) {
            wrapper.setPlotting(plot);
        }
        final List<Channel> plotChannels = getPlottingChannels();
        synchronized (PLOT_CHANNEL_IDs) {
            PLOT_CHANNEL_IDs.clear();
            for (final Channel channel : plotChannels) {
                PLOT_CHANNEL_IDs.add(channel.getId());
            }
        }
        EVENT_PROXY.plottingChannelsChanged(plotChannels);
    }
}
