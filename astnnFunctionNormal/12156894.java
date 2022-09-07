class BackupThread extends Thread {
    public String post(JDCConnection oConn, String sNewsGroupId, String sThreadId, Date dtStart, Date dtEnd, short iStatus, String sText) throws SQLException {
        if (DebugFile.trace) {
            DebugFile.writeln("Begin NewsMessage.post([Connection], " + sNewsGroupId + "," + sThreadId + ")");
            DebugFile.incIdent();
        }
        String sRetVal;
        remove(DB.gu_newsgrp);
        if (sNewsGroupId != null) put(DB.gu_newsgrp, sNewsGroupId);
        remove(DB.gu_thread_msg);
        if (sThreadId != null) put(DB.gu_thread_msg, sThreadId);
        remove(DB.dt_start);
        if (dtStart != null) put(DB.dt_start, dtStart);
        remove(DB.dt_end);
        if (dtEnd != null) put(DB.dt_end, dtEnd);
        remove(DB.id_status);
        put(DB.id_status, iStatus);
        remove(DB.tx_msg);
        if (sText != null) put(DB.tx_msg, sText);
        if (store(oConn)) sRetVal = getString(DB.gu_msg); else sRetVal = null;
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End NewsMessage.post() : " + sRetVal);
        }
        return sRetVal;
    }
}
