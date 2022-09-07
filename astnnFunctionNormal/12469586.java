class BackupThread extends Thread {
    public DBIDInt addRDBLongObject(RDBLongObject lobj, String table) throws RDFRDBException {
        DBIDInt longObjID = null;
        try {
            int argi = 1;
            boolean save = m_dbcon.getConnection().getAutoCommit();
            String opname = (lobj.tail.length() > 0) ? "insertLongObjectEmptyTail" : "insertLongObject";
            PreparedStatement ps = m_sql.getPreparedSQLStatement(opname, table);
            int dbid = 0;
            if (PRE_ALLOCATE_ID) {
                dbid = getInsertID(table);
                ps.setInt(argi++, dbid);
                longObjID = wrapDBID(new Integer(dbid));
            }
            ps.setString(argi++, lobj.head);
            if (lobj.tail.length() > 0) {
                ps.setLong(argi++, lobj.hash);
            } else {
                ps.setNull(argi++, java.sql.Types.BIGINT);
            }
            ps.executeUpdate();
            m_sql.returnPreparedSQLStatement(ps);
            if (lobj.tail.length() > 0) {
                if (!xactOp(xactIsActive)) {
                    m_dbcon.getConnection().setAutoCommit(false);
                }
                opname = "getEmptyBLOB";
                String cmd = m_sql.getSQLStatement(opname, table, longObjID.getID().toString());
                Statement lobStmt = m_sql.getConnection().createStatement();
                ResultSet lrs = lobStmt.executeQuery(cmd);
                lrs.next();
                BLOB blob = ((OracleResultSet) lrs).getBLOB(1);
                OutputStream outstream = blob.getBinaryOutputStream();
                int size = blob.getBufferSize();
                int length = -1;
                InputStream instream = new StringBufferInputStream(lobj.tail);
                byte[] buffer = new byte[size];
                while ((length = instream.read(buffer)) != -1) outstream.write(buffer, 0, length);
                if (blob.isOpen()) blob.close();
                instream.close();
                outstream.close();
                lobStmt.close();
                if (!xactOp(xactIsActive)) {
                    m_dbcon.getConnection().setAutoCommit(save);
                }
            }
            if (!PRE_ALLOCATE_ID) {
                dbid = getInsertID(table);
                longObjID = wrapDBID(new Integer(dbid));
            }
        } catch (Exception e1) {
            System.out.println("Problem on long object (l=" + lobj.head + ") " + e1);
            throw new RDFRDBException("Failed to add long object ", e1);
        }
        return longObjID;
    }
}
