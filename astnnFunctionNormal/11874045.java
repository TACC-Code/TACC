class BackupThread extends Thread {
    private void savePeriodData(Vector periods) throws Exception {
        String period_list = getParam("period_list");
        String schema_code = getParam("schema_code");
        if ("".equals(schema_code)) throw new Exception("Empty string...");
        Db db = null;
        String sql = "";
        Connection conn = null;
        try {
            db = new Db();
            conn = db.getConnection();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            conn.setAutoCommit(false);
            boolean found = false;
            {
                r.add("period_schema_code");
                r.add("period_schema_code", schema_code);
                sql = r.getSQLSelect("period_root");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true;
            }
            if (!found) {
                r.clear();
                r.add("period_schema_code", schema_code);
                r.add("period_root_id", schema_code);
                r.add("path_no", 0);
                sql = r.getSQLInsert("period_root");
                stmt.executeUpdate(sql);
            }
            {
                sql = "DELETE FROM period WHERE period_root_id = '" + schema_code + "'";
                stmt.executeUpdate(sql);
            }
            savePeriod(schema_code, periods, stmt, r, sql);
            conn.commit();
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
