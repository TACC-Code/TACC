class BackupThread extends Thread {
    public void get(StringTokenizer objStrTok, SessionThread objThread) throws Exception {
        String strID;
        SRCPDaemon objDaemon = objThread.getSRCPDaemon();
        int intID;
        String strOutBuffer = null;
        String strRetBuffer;
        try {
            strID = objStrTok.nextToken();
        } catch (Exception e) {
            throw new ExcListToShort();
        }
        if (objStrTok.hasMoreElements()) {
            throw new ExcListToLong();
        }
        int intSearchId = 0;
        try {
            intID = Integer.parseInt(strID);
            while (objDaemon.getSessionThread(intSearchId).getConnectionId() != intID) {
                intSearchId++;
            }
            strOutBuffer = objDaemon.getSessionThread(intSearchId).getInfo();
            objThread.writeAck(Declare.intInfoMin, strOutBuffer);
        } catch (Exception e) {
            throw new ExcWrongValue();
        }
    }
}
