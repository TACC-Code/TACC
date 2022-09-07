class BackupThread extends Thread {
    public int threadTemplate(StringBuffer querystr, String sbtid) throws Exception {
        int num = -1;
        querystr.append(" where t.tid in (");
        Object[] tids = sbtid.split(",");
        for (int i = 0; i < tids.length; i++) {
            querystr.append(tids[i].toString());
            querystr.append(",");
        }
        String str = querystr.substring(0, querystr.length() - 1);
        str = str + ")";
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery(str);
            num = query.executeUpdate();
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) {
                tr.rollback();
            }
            he.printStackTrace();
        }
        return num;
    }
}
