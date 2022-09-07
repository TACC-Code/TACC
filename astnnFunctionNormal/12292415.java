class BackupThread extends Thread {
    private Hashtable add() throws Exception {
        String exam_id = getParam("exam_id");
        String exam_name = getParam("exam_name");
        Hashtable data = new Hashtable();
        if ("".equals(exam_id.trim())) throw new Exception("Exam Id empty");
        if ("".equals(exam_name.trim())) throw new Exception("Exam Name empty");
        Db db = null;
        String sql = "";
        Connection conn = null;
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            SQLRenderer r = new SQLRenderer();
            boolean found = false;
            {
                r.add("adm_exam_name");
                r.add("adm_exam_id", exam_id);
                sql = r.getSQLSelect("adm_exam");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true;
            }
            if (found) {
                r.clear();
                r.add("adm_exam_name", exam_name);
                r.update("adm_exam_id", exam_id);
                sql = r.getSQLUpdate("adm_exam");
                stmt.executeUpdate(sql);
            } else {
                r.clear();
                r.add("adm_exam_id", exam_id);
                r.add("adm_exam_name", exam_name);
                sql = r.getSQLInsert("adm_exam");
                stmt.executeUpdate(sql);
            }
            conn.commit();
            data.put("id", exam_id);
            data.put("name", exam_name);
            return data;
        } catch (SQLException sqlex) {
            try {
                conn.rollback();
            } catch (SQLException rollex) {
            }
            throw sqlex;
        } finally {
            if (db != null) db.close();
        }
    }
}
