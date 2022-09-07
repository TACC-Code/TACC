class BackupThread extends Thread {
    public void insertFaculty() throws java.sql.SQLException {
        prepareInsertStatement();
        int entriesChanged = insertStmnt.executeUpdate();
        if (entriesChanged != 1) {
            javax.swing.JOptionPane.showMessageDialog(null, "Insert failed");
            conn.rollback();
        } else {
            conn.commit();
        }
    }
}
