class BackupThread extends Thread {
    public void insert() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            String sql = "";
            DbRs rs = null;
            sql = "select * from techconeng where conid=? and engid=? and taskid=?";
            conn.prepare(sql);
            conn.setInt(1, getConid());
            conn.setString(2, getEngid());
            conn.setInt(3, getTaskid());
            rs = conn.executeQuery();
            if (rs.size() > 0) {
                setErr("请不要在同一合同中添加同一个没有创建合同的工程师");
                return;
            }
            sql = "select th.* from techconeng th " + "left join task t on t.taskid=th.taskid " + " where t.status !=3 and t.conid=? and t.engineerid=?";
            conn.prepare(sql);
            conn.setInt(1, getConid());
            conn.setString(2, getEngid());
            rs = conn.executeQuery();
            if (rs.size() > 0) {
                setErr("此工程师的工单还没有结束您暂时不能分配");
                return;
            }
            sql = "insert into techconeng (conid,engid,taskid) values (?,?,? )";
            conn.prepare(sql);
            conn.setInt(1, getConid());
            conn.setString(2, getEngid());
            conn.setInt(3, getTaskid());
            conn.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            setErr(ex.getMessage());
            try {
                conn.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            conn.close();
        }
    }
}
