class BackupThread extends Thread {
    public static void changeSequence(String usrlogin, String tab, String module, String pos) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();
            int sequence = 0;
            {
                sql = "SELECT sequence FROM user_module_template WHERE module_id = '" + module + "' AND tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    sequence = rs.getInt("sequence");
                }
            }
            String module2 = "";
            if ("down".equals(pos)) {
                sql = "SELECT module_id FROM user_module_template WHERE tab_id = '" + tab + "' AND user_login = '" + usrlogin + "' AND sequence = " + Integer.toString(++sequence);
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) module2 = rs.getString("module_id");
                if (!"".equals(module2)) {
                    sql = "UPDATE user_module_template SET sequence = " + sequence + " WHERE module_id = '" + module + "' AND tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                    stmt.executeUpdate(sql);
                    sql = "UPDATE user_module_template SET sequence = " + Integer.toString(--sequence) + " WHERE module_id = '" + module2 + "' AND tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                    stmt.executeUpdate(sql);
                }
            } else if ("up".equals(pos)) {
                sql = "SELECT module_id FROM user_module_template WHERE tab_id = '" + tab + "' AND user_login = '" + usrlogin + "' AND sequence = " + Integer.toString(--sequence);
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) module2 = rs.getString("module_id");
                if (!"".equals(module2)) {
                    sql = "UPDATE user_module_template SET sequence = " + sequence + " WHERE module_id = '" + module + "' AND tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                    stmt.executeUpdate(sql);
                    sql = "UPDATE user_module_template SET sequence = " + Integer.toString(++sequence) + " WHERE module_id = '" + module2 + "' AND tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
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
