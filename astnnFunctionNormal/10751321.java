class BackupThread extends Thread {
    public ChannelDAO getChannelDAO() {
        if (channelDAO == null) channelDAO = new ChannelDAO();
        return channelDAO;
    }
}
