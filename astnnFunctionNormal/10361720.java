class BackupThread extends Thread {
    public String getToken(String rendezvousPID, String rendezvousProgram) {
        String token = null;
        try {
            XMLConfigParser.readUrlHost();
            String url = XMLConfigParser.urlHost;
            String query;
            String param1 = "op=gettoken";
            String param2 = "pid=" + rendezvousPID;
            String param3 = "program=" + rendezvousProgram;
            query = String.format("%s&%s&%s", param1, param2, param3);
            openConnection(query, url);
            token = br.readLine();
            NetworkLog.logMsg(NetworkLog.LOG_DEBUG, this, "Token successfully acquired.");
            br.close();
        } catch (MalformedURLException e) {
            NetworkLog.logMsg(NetworkLog.LOG_ERROR, this, "Error" + e);
        } catch (IOException e) {
            NetworkLog.logMsg(NetworkLog.LOG_ERROR, this, e.getMessage());
        }
        return token;
    }
}
