class BackupThread extends Thread {
    public void updateStatus(long alarmId, int status) throws HibernateException {
        Session session = mpower_hibernate.HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        try {
            String hql = "update Alarm set Status = ? where AlarmId = ?";
            Query query = session.createQuery(hql);
            query.setInteger(0, status);
            query.setLong(1, alarmId);
            int rowCount = query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw e;
        } finally {
            mpower_hibernate.HibernateUtil.closeSession();
        }
    }
}
