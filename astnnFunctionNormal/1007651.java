class BackupThread extends Thread {
    public Channel getChannel(Channel channel) {
        Channel c = channels.get(channel.address);
        if (c != null) return c;
        return new Channel(channel.address, (short) 0);
    }
}
