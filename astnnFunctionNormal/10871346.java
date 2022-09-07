class BackupThread extends Thread {
    public void term(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        String strID = null;
        try {
            strID = objStrTok.nextToken();
        } catch (Exception e) {
            objThread.writeAck();
            objThread.doTerminate();
            return;
        }
        if (objStrTok.hasMoreElements()) {
            throw new ExcListToLong();
        }
        int intSearchId = 0;
        int intID = 0;
        SRCPDaemon objDaemon = objThread.getSRCPDaemon();
        try {
            intID = Integer.parseInt(strID);
            while (objDaemon.getSessionThread(intSearchId).getConnectionId() != intID) {
                intSearchId++;
            }
            objDaemon.getSessionThread(intSearchId).stopThread();
            objThread.writeAck();
        } catch (Exception e) {
            throw new ExcWrongValue();
        }
        ;
    }
}
