class BackupThread extends Thread {
    public void delete(long protocolsId) {
        Session session = mpower_hibernate.HibernateUtil.currentSession();
        Transaction transaction = session.beginTransaction();
        java.lang.System.out.println("protocolsId::" + protocolsId);
        try {
            org.hibernate.Query query = session.createQuery(" delete " + " from  " + " Protocols lr WHERE lr.id = ? ");
            query.setLong(0, protocolsId);
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            transaction.rollback();
            throw e;
        } finally {
            mpower_hibernate.HibernateUtil.closeSession();
        }
    }
}
