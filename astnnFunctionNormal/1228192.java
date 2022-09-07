class BackupThread extends Thread {
    public List getChannels(String query) {
        if (queries.getProperty(query) != null) {
            return this.getHibernateTemplate().find(queries.getProperty(query));
        } else {
            return this.getHibernateTemplate().find(query);
        }
    }
}
