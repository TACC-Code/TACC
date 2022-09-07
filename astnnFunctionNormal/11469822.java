class BackupThread extends Thread {
    public void triggerDisabled(final Trigger source) {
        try {
            Channel channel = source.getChannel();
            if (channel != null && correlator.hasSource(channel.channelName())) {
                correlator.removeChannel(channel);
            }
        } catch (Exception exception) {
            System.err.println(exception);
            exception.printStackTrace();
        }
        correlator.setCorrelationFilter(CorrelationFilterFactory.minCountFilter(1));
    }
}
