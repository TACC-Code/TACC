class BackupThread extends Thread {
    public static synchronized TVListService getService() {
        if (service == null) {
            pr("Initializing Service ...");
            service = new TVListService(DAOFactory.getChannelDAO(getHibernateSessionFactory()), DAOFactory.getBroadcastDAO(getHibernateSessionFactory()));
        }
        return service;
    }
}
