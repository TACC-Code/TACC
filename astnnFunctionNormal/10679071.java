class BackupThread extends Thread {
    private void add() throws Exception {
        String exam_id = getParam("exam_id");
        String subject_id = getParam("subject_id");
        String subject_name = getParam("subject_name");
        String grade_id = getParam("grade_id");
        if ("".equals(exam_id) || "".equals(subject_id) || "".equals(subject_name) || "".equals(grade_id)) throw new Exception("Can not have empty fields!");
        Db db = null;
        String sql = "";
        Connection conn = null;
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            boolean found = false;
            {
                r.add("adm_exam_id");
                r.add("adm_exam_id", exam_id);
                sql = r.getSQLSelect("adm_exam");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true;
            }
            if (!found) throw new Exception("Exam Id was invalid!");
            {
                r.clear();
                r.add("adm_subject_id");
                r.add("adm_subject_id", subject_id);
                sql = r.getSQLSelect("adm_exam_subject");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true; else found = false;
            }
            if (found) {
                r.clear();
                r.clear();
                r.add("adm_subject_name", subject_name);
                r.add("adm_grade_display_id", grade_id);
                r.update("adm_exam_id", exam_id);
                r.update("adm_subject_id", subject_id);
                sql = r.getSQLUpdate("adm_exam_subject");
                stmt.executeUpdate(sql);
            } else {
                r.clear();
                r.add("adm_exam_id", exam_id);
                r.add("adm_subject_id", subject_id);
                r.add("adm_subject_name", subject_name);
                r.add("adm_grade_display_id", grade_id);
                sql = r.getSQLInsert("adm_exam_subject");
                stmt.executeUpdate(sql);
            }
            conn.commit();
        } catch (DbException dbex) {
            throw dbex;
        } catch (SQLException sqlex) {
            try {
                conn.rollback();
            } catch (SQLException rollex) {
            }
        } finally {
            if (db != null) db.close();
        }
    }
}
