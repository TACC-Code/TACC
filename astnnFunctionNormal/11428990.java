class BackupThread extends Thread {
    public Integer updateNameImagetypes(List<Imagetypes> list) {
        Integer num = -1;
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("update Imagetypes as i set i.name = :name,i.displayorder=:displayorder where i.typeid = :typeid");
            for (int i = 0; i < list.size(); i++) {
                query.setString("name", list.get(i).getName());
                query.setShort("displayorder", list.get(i).getDisplayorder());
                query.setShort("typeid", list.get(i).getTypeid());
                num += query.executeUpdate();
            }
            session.flush();
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) tr.rollback();
            tr = null;
            he.printStackTrace();
            num = 0;
        }
        return num;
    }
}
