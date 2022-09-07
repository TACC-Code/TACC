class BackupThread extends Thread {
    private boolean doTransaction(String statement, Statement st, int id, int namespaceId, DTSPermission permit, char attrType, char editType) throws SQLException {
        conn.setAutoCommit(false);
        int defaultLevel = conn.getTransactionIsolation();
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        boolean success = false;
        int result = 0;
        try {
            WFPlugin wf = this.getConceptWF(permit);
            if (editType == WFPlugin.EDIT_DELETE) {
                wf.update(id, namespaceId, permit, attrType, editType);
                updateConceptRelatedWF(wf, permit, id, namespaceId);
            }
            if (statement == null) {
                result = ((PreparedStatement) st).executeUpdate();
            } else {
                result = st.executeUpdate(statement);
            }
            success = (result == 1) ? true : false;
            if (!success) {
                conn.rollback();
                return success;
            }
            if (editType != WFPlugin.EDIT_DELETE) {
                wf.update(id, namespaceId, permit, attrType, editType);
            }
            conn.commit();
            return success;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } catch (Exception e) {
            conn.rollback();
            throw new SQLException("unable to update concept: id " + id + " namespaceId: " + namespaceId + " edit mode:" + editType);
        } finally {
            conn.setTransactionIsolation(defaultLevel);
            conn.setAutoCommit(true);
        }
    }
}
