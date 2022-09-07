class BackupThread extends Thread {
    @Override
    public int updateAttribute(String attribute, Object newValue) throws RecordException, RecordValidationException, RecordValidationSyntax, FieldOrMethodNotFoundException {
        Connection conn = ConnectionManager.getConnection();
        LoggableStatement pStat = null;
        Class<? extends Record> actualClass = this.getClass();
        String tableName = TableNameResolver.getTableName(actualClass);
        Field id = null;
        Field toChange = null;
        try {
            id = FieldHandler.findField(this.getClass(), "id");
            toChange = FieldHandler.findField(this.getClass(), attribute);
            toChange.set(this, newValue);
        } catch (Exception e) {
            throw new RecordException(e);
        }
        doValidation(toChange, true);
        String sql = "update " + tableName + " set " + attribute + " = :" + attribute + " where id = :id";
        StatementBuilder builder = new StatementBuilder(sql);
        try {
            builder.set(":id", FieldHandler.getValue(id, this));
            builder.set(":" + attribute, FieldHandler.getValue(toChange, this));
            pStat = builder.getPreparedStatement(conn);
            log.log(pStat.getQueryString());
            int res = pStat.executeUpdate();
            return res;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                throw new RecordException("Error executing rollback");
            }
            throw new RecordException(e);
        } finally {
            try {
                if (pStat != null) {
                    pStat.close();
                }
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                throw new RecordException("Error closing connection");
            }
        }
    }
}
