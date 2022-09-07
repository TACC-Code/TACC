class BackupThread extends Thread {
    public boolean load(JDCConnection oConn, Object aPK[]) throws SQLException {
        CallableStatement oStmt;
        String sNmMicrosite;
        boolean bRetVal;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin MicrositeDB.load([Connection], {" + aPK[0] + "}");
            DebugFile.incIdent();
        }
        if (oConn.getDataBaseProduct() == JDCConnection.DBMS_ORACLE || oConn.getDataBaseProduct() == JDCConnection.DBMS_MSSQL) {
            if (DebugFile.trace) DebugFile.writeln("Connection.prepareCall({ call k_sp_read_microsite ('" + aPK[0] + "',?,?,?,?) }");
            oStmt = oConn.prepareCall("{ call k_sp_read_microsite (?,?,?,?,?) }");
            clear();
            oStmt.setObject(1, aPK[0], Types.CHAR);
            oStmt.registerOutParameter(2, Types.INTEGER);
            oStmt.registerOutParameter(3, Types.VARCHAR);
            oStmt.registerOutParameter(4, Types.VARCHAR);
            oStmt.registerOutParameter(5, Types.CHAR);
            if (DebugFile.trace) DebugFile.writeln("CallableStatement.execute()");
            oStmt.execute();
            sNmMicrosite = oStmt.getString(3);
            bRetVal = (null != sNmMicrosite);
            put(DB.gu_microsite, aPK[0]);
            if (bRetVal) {
                put(DB.id_app, oStmt.getInt(2));
                put(DB.nm_microsite, oStmt.getString(3));
                put(DB.path_metadata, oStmt.getString(4));
                if (oStmt.getObject(5) != null) put(DB.gu_workarea, oStmt.getString(5).trim());
            }
            oStmt.close();
        } else bRetVal = super.load(oConn, aPK);
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End MicrositeDB.load() : " + String.valueOf(bRetVal));
        }
        return bRetVal;
    }
}
