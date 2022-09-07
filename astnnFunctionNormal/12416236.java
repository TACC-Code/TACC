class BackupThread extends Thread {
    public void donotupdatemember(Boolean b, String sbtid) {
        StringBuffer querystr = new StringBuffer("delete from Threads as t");
        StringBuffer queryPosts = new StringBuffer("delete from Posts as t");
        StringBuffer attachPosts = new StringBuffer("delete from Attachments as t");
        if (b == false) {
            Transaction tr = null;
            try {
                Session session = HibernateUtil.getSessionFactory().openSession();
                tr = session.beginTransaction();
                StringBuffer sb = new StringBuffer("from Threads as t where t.tid in (");
                Object[] tids = sbtid.split(",");
                for (int i = 0; i < tids.length; i++) {
                    sb.append(tids[i].toString());
                    sb.append(",");
                }
                String str = sb.substring(0, sb.length() - 1);
                str = str + ")";
                Query query = session.createQuery(str);
                List<Threads> threadsList = query.list();
                session.flush();
                for (int i = 0; i < threadsList.size(); i++) {
                    Integer num = threadsList.get(i).getAuthorid();
                    SQLQuery sqlquery = session.createSQLQuery("update Member as m set m.posts= m.posts-1,m.credits = m.credits-1 where m.uid =" + threadsList.get(i).getAuthorid() + "");
                    sqlquery.executeUpdate();
                }
                tr.commit();
            } catch (HibernateException he) {
                if (tr != null) {
                    tr.rollback();
                }
                he.printStackTrace();
            }
        }
        int num = -1;
        try {
            num = threadTemplate(querystr, sbtid);
            num = threadTemplate(queryPosts, sbtid);
            num = threadTemplate(attachPosts, sbtid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
