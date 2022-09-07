class BackupThread extends Thread {
    public Channel getChannel(final String name) {
        Channel channel;
        synchronized (channels) {
            channel = (Channel) channels.get(name);
            if (channel == null) {
                channel = new Channel();
                channels.put(name, channel);
            }
        }
        return channel;
    }
}
