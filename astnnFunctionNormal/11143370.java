class BackupThread extends Thread {
    public int modifyAllValidating(byte status) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query query = session.createQuery("update Validating set status = ?");
            query.setByte(0, status);
            int count = query.executeUpdate();
            tr.commit();
            return count;
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return 0;
    }
}
