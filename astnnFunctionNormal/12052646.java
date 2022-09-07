class BackupThread extends Thread {
    public void setFaculty() throws java.sql.SQLException {
        prepareSetStatement();
        getStmnt.setInt(1, idNumber);
        int entriesChanged = getStmnt.executeUpdate();
        if (entriesChanged > 1) {
            javax.swing.JOptionPane.showMessageDialog(null, "Duplicate primary keys");
            conn.rollback();
        } else {
            conn.commit();
        }
    }
}
