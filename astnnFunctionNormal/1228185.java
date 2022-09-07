class BackupThread extends Thread {
    public Channel getChannel(Long id) {
        return (Channel) this.getHibernateTemplate().load(Channel.class, id);
    }
}
