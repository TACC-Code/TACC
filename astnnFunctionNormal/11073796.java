class BackupThread extends Thread {
    private void setChannelWrapper(final int index, final ChannelWrapper wrapper) {
        setChannelEnable(index, false);
        CHANNEL_WRAPPERS.set(index, wrapper);
        CORRELATOR.addChannel(wrapper.getChannel());
        setChannelEnable(index, true);
    }
}
