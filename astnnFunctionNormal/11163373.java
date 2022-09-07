class BackupThread extends Thread {
    public static IChannelSourceDAO getChannelSourceDAO(IHTTPClientSessionFactory sessionFactory) {
        IChannelSourceDAO dao = new ChannelHTTPClientDAO();
        dao.setSessionFactory(sessionFactory);
        return dao;
    }
}
