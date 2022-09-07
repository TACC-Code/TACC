class BackupThread extends Thread {
    private void remove(String schema_id) throws Exception {
        Db db = null;
        String sql = "";
        Connection conn = null;
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();
            sql = "DELETE FROM period where period_root_id = '" + schema_id + "' ";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM period_root WHERE period_root_id = '" + schema_id + "' ";
            stmt.executeUpdate(sql);
            conn.commit();
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
