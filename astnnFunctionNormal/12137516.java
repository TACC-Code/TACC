class BackupThread extends Thread {
    public void removeChannel(String name) {
        if (name == null) {
            return;
        }
        AbstractIRCChannel channel = getChannel(name);
        if (channel == null) {
            return;
        }
        channels.remove(channel);
        fireChannelRemovedEvent(channel);
    }
}
