class BackupThread extends Thread {
    public ChannelUser getChannelUser(int i) {
        return (ChannelUser) channelUsers.get(i);
    }
}
