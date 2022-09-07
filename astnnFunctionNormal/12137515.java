class BackupThread extends Thread {
    public AbstractIRCChannel addChannel(String name) {
        if (name == null) {
            return null;
        }
        AbstractIRCChannel channel = getChannel(name);
        if (channel != null) {
            return channel;
        }
        channel = getChannelImpl(name);
        if (channel == null) {
            return null;
        }
        channels.put(name, channel);
        fireChannelCreatedEvent(channel);
        return channel;
    }
}
