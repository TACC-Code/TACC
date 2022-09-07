class BackupThread extends Thread {
    public String getChannelID(final int index) {
        final Channel channel = getChannel(index);
        return channel != null ? channel.getId() : null;
    }
}
