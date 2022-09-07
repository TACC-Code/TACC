class BackupThread extends Thread {
    public int executeTransaction(String[] queries) {
        int done;
        try {
            con.setAutoCommit(false);
            for (int i = 0; i < queries.length; i++) {
                done = stmt.executeUpdate(queries[i]);
            }
            con.commit();
            con.setAutoCommit(true);
            done = 1;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (Exception ex) {
            }
            done = -1;
        }
        return done;
    }
}
