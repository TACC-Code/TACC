class BackupThread extends Thread {
    protected void addStringTimingChannel(final String title, final String handle) {
        try {
            final Channel channel = TimingCenter.getDefaultTimingCenter().getChannel(handle);
            final ChannelWrapper wrapper = ChannelWrapper.getStringChannelWrapper(title, channel);
            if (wrapper != null) {
                SUMMARY_CHANNEL_WRAPPERS.add(ChannelWrapper.getStringChannelWrapper(title, channel));
            }
        } catch (NoSuchChannelException exception) {
            System.err.println("No timing channel found for handle:  " + handle);
        }
    }
}
