class BackupThread extends Thread {
    public ChannelIF getById(long channelId) {
        Iterator it = getChannels().iterator();
        while (it.hasNext()) {
            ChannelIF channel = (ChannelIF) it.next();
            if (channel.getId() == channelId) {
                return channel;
            }
        }
        return null;
    }
}
