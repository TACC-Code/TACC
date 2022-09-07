class BackupThread extends Thread {
    protected int getChannelIndex(final int row) {
        int channelIndex = row - getShow().getNumberOfSubmasters() - 1;
        Groups groups = context.getShow().getGroups();
        if (groups.isEnabled()) {
            Channel channel = groups.getChannel(channelIndex);
            channelIndex = channel.getId();
        }
        return channelIndex;
    }
}
