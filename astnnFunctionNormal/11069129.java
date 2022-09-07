class BackupThread extends Thread {
    public static void fixModuleSequence(String usrlogin, String tab) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();
            boolean fix = true;
            if (fix) {
                Vector v = new Vector();
                sql = "SELECT module_id FROM user_module_template WHERE tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String module_id = rs.getString("module_id");
                    v.addElement(module_id);
                }
                for (int i = 0; i < v.size(); i++) {
                    String module_id = (String) v.elementAt(i);
                    sql = "UPDATE user_module_template SET sequence = " + Integer.toString(i + 1) + " WHERE module_id = '" + module_id + "' AND tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                    stmt.executeUpdate(sql);
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException exr) {
            }
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if (db != null) db.close();
        }
    }
}
