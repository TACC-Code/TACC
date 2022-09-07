class BackupThread extends Thread {
    @Override
    public void addChannel(Rss20ChannelImpl channel) {
        if (getChannelList() == null) {
            ArrayList<Rss20ChannelImpl> list = new ArrayList<Rss20ChannelImpl>();
            setChannelList(list);
        }
        getChannelList().add(channel);
    }
}
