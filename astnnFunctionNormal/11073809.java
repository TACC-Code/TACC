class BackupThread extends Thread {
    public String getChannelName(final int index) {
        final Channel channel = getChannel(index);
        return channel != null ? channel.channelName() : null;
    }
}
