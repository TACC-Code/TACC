class BackupThread extends Thread {
    public static IChannelDAO getChannelDAO() {
        if (channelDAO == null) {
            channelDAO = new ChannelHibernateDAO();
            channelDAO.setSessionFactory(defaultHibernateSessionFactory);
        }
        return channelDAO;
    }
}
