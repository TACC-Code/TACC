class BackupThread extends Thread {
    public Channel getChannel(final int index) {
        final ChannelWrapper wrapper = getChannelWrapper(index);
        return wrapper != null ? wrapper.getChannel() : null;
    }
}
