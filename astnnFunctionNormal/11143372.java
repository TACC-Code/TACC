class BackupThread extends Thread {
    public boolean deleteProfile(Short filedId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query query = session.createQuery("delete from Profilefields p where p.fieldid = ?");
            query.setShort(0, filedId);
            query.executeUpdate();
            tr.commit();
            return true;
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }
}
