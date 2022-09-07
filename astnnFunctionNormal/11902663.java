class BackupThread extends Thread {
    public void delete(String word, int row) throws FidoDatabaseException {
        try {
            Connection conn = null;
            Statement stmt = null;
            try {
                conn = fido.util.FidoDataSource.getConnection();
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                int max = max(stmt, word);
                String sql = "delete from Dictionary where Word = '" + word + "' and SenseNumber = " + row;
                stmt.executeUpdate(sql);
                for (int i = row; i < max; ++i) {
                    stmt.executeUpdate("update Dictionary set SenseNumber = " + i + " where SenseNumber = " + (i + 1) + " and Word = '" + word + "'");
                }
                conn.commit();
            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                throw e;
            } finally {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException e) {
            throw new FidoDatabaseException(e);
        }
    }
}
