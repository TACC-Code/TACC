class BackupThread extends Thread {
    public static IChannelDAO getChannelDAO(IHibernateSessionFactory sessionFactory) {
        IChannelDAO dao = new ChannelHibernateDAO();
        dao.setSessionFactory(sessionFactory);
        return dao;
    }
}
