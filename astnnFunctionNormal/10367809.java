class BackupThread extends Thread {
    private int getChannelNum(String network, String channel) throws BotSecurityException {
        Connection dbconn;
        Statement stmt;
        String query;
        ResultSet rslt;
        int channelNum;
        try {
            dbconn = getConnection();
            stmt = dbconn.createStatement();
            query = "SELECT id FROM channels WHERE name=\'" + channel + "\' AND network=\'" + network + "\'";
            rslt = stmt.executeQuery(query);
            if (rslt.next()) channelNum = rslt.getInt(1); else throw new BQLException("query gave no result: " + query);
        } catch (Exception e) {
            throw new BotSecurityException("channel " + channel + " on " + network + " doesn't exist");
        }
        return channelNum;
    }
}
