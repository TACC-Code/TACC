class BackupThread extends Thread {
    public Integer deleteImagetypesAll(Short[] ids) {
        Transaction tr = null;
        Integer num = 0;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("delete from Smilies as s where s.typeid in (:typeids)");
            query.setParameterList("typeids", ids);
            query.executeUpdate();
            query = session.createQuery("delete from Imagetypes as i where i.typeid in (:typeids)");
            query.setParameterList("typeids", ids);
            query.executeUpdate();
            session.flush();
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) tr.rollback();
            tr = null;
        }
        return num;
    }
}
