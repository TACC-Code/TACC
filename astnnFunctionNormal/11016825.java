class BackupThread extends Thread {
    public static void saveModules(String usrlogin, String tabid, Vector allmodules) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            conn = db.getConnection();
            Statement stmt = db.getStatement();
            conn.setAutoCommit(false);
            Vector checkedModules = new Vector();
            for (int i = 0; i < allmodules.size(); i++) {
                Module2 module = (Module2) allmodules.elementAt(i);
                if (module.getMarked()) {
                    checkedModules.addElement(module.getId());
                }
            }
            Vector userModules = new Vector();
            {
                sql = "SELECT module_id FROM user_module WHERE user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "'";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String id = rs.getString("module_id");
                    userModules.addElement(id);
                }
            }
            Vector deletedModules = new Vector();
            for (int i = 0; i < userModules.size(); i++) {
                if (!checkedModules.contains((String) userModules.elementAt(i))) {
                    deletedModules.addElement((String) userModules.elementAt(i));
                }
            }
            Vector addedModules = new Vector();
            for (int i = 0; i < checkedModules.size(); i++) {
                if (!userModules.contains((String) checkedModules.elementAt(i))) {
                    addedModules.addElement((String) checkedModules.elementAt(i));
                }
            }
            for (int i = 0; i < deletedModules.size(); i++) {
                String id = (String) deletedModules.elementAt(i);
                int sequence = 0;
                sql = "SELECT sequence FROM user_module WHERE module_id = '" + id + "' AND user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' ";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) sequence = rs.getInt("sequence");
                sql = "DELETE FROM user_module WHERE module_id = '" + id + "' AND user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' ";
                stmt.executeUpdate(sql);
                sql = "UPDATE user_module SET sequence = sequence - 1 WHERE sequence > " + sequence + " AND user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' ";
                stmt.executeUpdate(sql);
            }
            {
                int maxseq = 0;
                sql = "SELECT MAX(sequence) AS seq FROM user_module WHERE user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' ";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) maxseq = rs.getInt("seq");
                for (int i = 0; i < addedModules.size(); i++) {
                    String id = (String) addedModules.elementAt(i);
                    sql = "INSERT INTO user_module (tab_id, module_id, user_login, sequence) VALUES (" + "'" + tabid + "', '" + id + "', '" + usrlogin + "', " + Integer.toString(++maxseq) + ")";
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
