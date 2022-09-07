class BackupThread extends Thread {
    public void triggerEnabled(final Trigger source) {
        final Channel channel = source.getChannel();
        final String channelName = channel.channelName();
        correlator.addChannel(channel, source.getRecordFilter());
        correlator.setCorrelationFilter(new CorrelationFilter() {

            public boolean accept(final Correlation correlation, final int fullCount) {
                return correlation.isCorrelated(channelName) && (correlation.numRecords() > 1);
            }
        });
    }
}
