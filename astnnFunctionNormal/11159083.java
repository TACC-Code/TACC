class BackupThread extends Thread {
    public synchronized Channel getChannel(String channelName) {
        Object channel = myChannels.get(channelName);
        if (channel == null) {
            throw new IllegalArgumentException("No such channel: " + channelName);
        }
        return (Channel) channel;
    }
}
