class BackupThread extends Thread {
    public int executeSQL(Statement[] statements, String[] sql_statements, AceThread cthread, Object user_parm, boolean return_multiple_query_results) {
        int next_id = -1;
        Thread caller = cthread;
        if (caller == null) {
            caller = Thread.currentThread();
        }
        if ((caller instanceof AceThread) == false) {
            writeErrorMessage("The calling thread must be an instance of AceThread", null);
            return -1;
        }
        try {
            if (statements == null) {
                if (return_multiple_query_results == true) {
                    statements = new Statement[sql_statements.length];
                    for (int i = 0; i < statements.length; i++) {
                        statements[i] = dbConnection.createStatement();
                    }
                } else {
                    statements = new Statement[1];
                    statements[0] = dbConnection.createStatement();
                }
            }
        } catch (SQLException ex) {
            writeErrorMessage("Failed to create an SQL Statement object : " + ex.getMessage(), ex);
            return -1;
        }
        try {
            synchronized (nextOperationIdLock) {
                next_id = nextOperationId++;
            }
            AceSQLThread sql = new AceSQLThread(next_id, statements, sql_statements, (AceThread) caller, user_parm);
            sql.start();
        } catch (IOException ex1) {
            writeErrorMessage("Could not create thread to execute the SQL statement : " + ex1.getMessage(), ex1);
            return -1;
        }
        return next_id;
    }
}
