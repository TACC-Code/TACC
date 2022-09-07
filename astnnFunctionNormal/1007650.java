class BackupThread extends Thread {
    public Channel getChannel(short address) {
        Channel c = channels.get(address);
        if (c != null) return c;
        return new Channel(address, (short) 0);
    }
}
