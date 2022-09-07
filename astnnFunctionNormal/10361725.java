class BackupThread extends Thread {
    public void updateTime(String rendezvousIPv4, String rendezvousIPv6, int rendezvousPort, String rendezvousPID, String rendezvousProgram, String rendezvousMD5) {
        String line;
        try {
            XMLConfigParser.readUrlHost();
            String url = XMLConfigParser.urlHost;
            Date currenttime = new Date();
            String query;
            String param1 = "op=updatetime";
            String param2 = "addr=" + rendezvousIPv4;
            String param3 = "addr2=" + rendezvousIPv6;
            String param4 = "port=" + rendezvousPort;
            String param5 = "pid=" + rendezvousPID;
            String param6 = "program=" + rendezvousProgram;
            String param7 = "md5=" + rendezvousMD5;
            String param8 = "time=" + currenttime.getTime();
            String param9 = "token=" + getToken(rendezvousPID, rendezvousProgram);
            query = String.format("%s&%s&%s&%s&%s&%s&%s&%s&%s", param1, param2, param3, param4, param5, param6, param7, param8, param9);
            openConnection(query, url);
            line = br.readLine();
            NetworkLog.logMsg(NetworkLog.LOG_INFO, this, " (Update check) Response of the server: " + line);
            br.close();
        } catch (MalformedURLException e) {
            NetworkLog.logMsg(NetworkLog.LOG_ERROR, this, "Error" + e);
        } catch (IOException e) {
            NetworkLog.logMsg(NetworkLog.LOG_ERROR, this, e.getMessage());
        }
    }
}
