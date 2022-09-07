class BackupThread extends Thread {
    public boolean store(JDCConnection oConn) throws SQLException {
        boolean bNewMsg;
        boolean bRetVal;
        String sMsgId;
        int nThreadMsgs;
        ResultSet oRSet;
        Statement oStmt;
        CallableStatement oCall;
        String sSQL;
        Timestamp dtNow = new Timestamp(DBBind.getTime());
        if (DebugFile.trace) {
            DebugFile.writeln("Begin NewsMessage.store([Connection])");
            DebugFile.incIdent();
        }
        if (!AllVals.containsKey(DB.gu_msg)) {
            bNewMsg = true;
            sMsgId = Gadgets.generateUUID();
            put(DB.gu_msg, sMsgId);
        } else {
            bNewMsg = false;
            sMsgId = getString(DB.gu_msg);
        }
        if (!AllVals.containsKey(DB.id_status)) put(DB.id_status, STATUS_PENDING);
        if (!AllVals.containsKey(DB.gu_thread_msg)) if (AllVals.containsKey(DB.gu_parent_msg)) {
            oStmt = oConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (DebugFile.trace) DebugFile.writeln("Statement.executeQuery(SELECT " + DB.gu_thread_msg + " FROM " + DB.k_newsmsgs + " WHERE " + DB.gu_msg + "='" + getStringNull(DB.gu_parent_msg, "null") + "'");
            oRSet = oStmt.executeQuery("SELECT " + DB.gu_thread_msg + " FROM " + DB.k_newsmsgs + " WHERE " + DB.gu_msg + "='" + getString(DB.gu_parent_msg) + "'");
            if (oRSet.next()) put(DB.gu_thread_msg, oRSet.getString(1)); else put(DB.gu_thread_msg, sMsgId);
            oRSet.close();
            oStmt.close();
        } else put(DB.gu_thread_msg, sMsgId);
        if (oConn.getDataBaseProduct() == JDCConnection.DBMS_POSTGRESQL) {
            sSQL = "SELECT k_sp_count_thread_msgs ('" + getString(DB.gu_thread_msg) + "')";
            oStmt = oConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (DebugFile.trace) DebugFile.writeln("Statement.executeQuery(" + sSQL + ")");
            oRSet = oStmt.executeQuery(sSQL);
            oRSet.next();
            nThreadMsgs = oRSet.getInt(1);
            oRSet.close();
            oStmt.close();
        } else {
            sSQL = "{ call k_sp_count_thread_msgs('" + getString(DB.gu_thread_msg) + "',?)}";
            if (DebugFile.trace) DebugFile.writeln("CallableStatement.prepareCall(" + sSQL + ")");
            oCall = oConn.prepareCall(sSQL);
            oCall.registerOutParameter(1, Types.INTEGER);
            oCall.execute();
            nThreadMsgs = oCall.getInt(1);
            oCall.close();
        }
        replace(DB.nu_thread_msgs, ++nThreadMsgs);
        if (!AllVals.containsKey(DB.dt_start)) put(DB.dt_start, dtNow);
        if (!AllVals.containsKey(DB.id_msg_type)) put(DB.id_msg_type, "TXT");
        replace(DB.dt_published, dtNow);
        bRetVal = super.store(oConn);
        oStmt = oConn.createStatement();
        sSQL = "UPDATE " + DB.k_newsmsgs + " SET " + DB.nu_thread_msgs + "=" + String.valueOf(nThreadMsgs) + " WHERE " + DB.gu_thread_msg + "='" + getString(DB.gu_thread_msg) + "'";
        if (DebugFile.trace) DebugFile.writeln("Statement.executeUpdate(" + sSQL + ")");
        oStmt.executeUpdate(sSQL);
        oStmt.close();
        if (!AllVals.containsKey(DB.gu_newsgrp) && !sMsgId.equals(get(DB.gu_thread_msg))) {
            sSQL = "SELECT " + DB.gu_category + " FROM " + DB.k_x_cat_objs + " WHERE " + DB.gu_object + "='" + getStringNull(DB.gu_thread_msg, "null") + "'";
            oStmt = oConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (DebugFile.trace) DebugFile.writeln("Statement.executeQuery(" + sSQL + ")");
            oRSet = oStmt.executeQuery(sSQL);
            if (oRSet.next()) put(DB.gu_newsgrp, oRSet.getString(1));
            oRSet.close();
            oStmt.close();
        }
        if (bRetVal && AllVals.containsKey(DB.gu_newsgrp)) {
            if (DebugFile.trace) DebugFile.writeln("Category.store() && containsKey(DB.gu_newsgrp)");
            if (bNewMsg) {
                if (DebugFile.trace) DebugFile.writeln("new message");
                boolean bHasLastUpdate;
                try {
                    bHasLastUpdate = (((DBBind) (oConn.getPool().getDatabaseBinding())).getDBTable(DB.k_newsgroups).getColumnByName(DB.dt_last_update) != null);
                } catch (NullPointerException npe) {
                    bHasLastUpdate = true;
                }
                if (bHasLastUpdate) {
                    PreparedStatement oUpdt = null;
                    sSQL = "UPDATE " + DB.k_newsgroups + " SET " + DB.dt_last_update + "=? WHERE " + DB.gu_newsgrp + "=?";
                    if (DebugFile.trace) DebugFile.writeln("Connection.prepareStatement (" + sSQL + ")");
                    oUpdt = oConn.prepareStatement(sSQL);
                    oUpdt.setTimestamp(1, new Timestamp(new Date().getTime()));
                    oUpdt.setObject(2, AllVals.get(DB.gu_newsgrp), java.sql.Types.VARCHAR);
                    oUpdt.executeUpdate();
                    oUpdt.close();
                    oUpdt = null;
                }
            } else {
                sSQL = "DELETE FROM " + DB.k_x_cat_objs + " WHERE " + DB.gu_category + "='" + getString(DB.gu_newsgrp) + "' AND " + DB.gu_object + "='" + sMsgId + "'";
                oStmt = oConn.createStatement();
                if (DebugFile.trace) DebugFile.writeln("Statement.execute(" + sSQL + ")");
                oStmt.execute(sSQL);
                oStmt.close();
            }
            sSQL = "INSERT INTO " + DB.k_x_cat_objs + "(" + DB.gu_category + "," + DB.gu_object + "," + DB.id_class + "," + DB.bi_attribs + "," + DB.od_position + ") VALUES ('" + getString(DB.gu_newsgrp) + "','" + sMsgId + "'," + String.valueOf(NewsMessage.ClassId) + ",0,NULL)";
            oStmt = oConn.createStatement();
            if (DebugFile.trace) DebugFile.writeln("Statement.execute(" + sSQL + ")");
            oStmt.execute(sSQL);
            oStmt.close();
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End NewsMessage.store() : " + String.valueOf(bRetVal));
        }
        return bRetVal;
    }
}
