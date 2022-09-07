class BackupThread extends Thread {
    public static IChannelSourceDAO getChannelSourceDAO() {
        if (channelSourceDAO == null) {
            channelSourceDAO = new ChannelHTTPClientDAO();
            channelSourceDAO.setSessionFactory(defaultHTTPClientSessionFactory);
        }
        return channelSourceDAO;
    }
}
