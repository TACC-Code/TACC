class BackupThread extends Thread {
    public Channel getChannel() {
        return AppContext.getChannelManager().getChannel(getName());
    }
}
