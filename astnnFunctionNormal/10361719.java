class BackupThread extends Thread {
    public String getAccountSecretKey(String id) throws IOException {
        String secret = null;
        XMLConfigParser.readUrlSecretKey();
        XMLConfigParser.readHttpsCredentials();
        String url = XMLConfigParser.urlSecretKey;
        if (!XMLConfigParser.httpsUsername.isEmpty() && !XMLConfigParser.httpsPassword.isEmpty()) {
            url = "https://" + XMLConfigParser.httpsUsername + ":" + XMLConfigParser.httpsPassword + "@" + url.substring(7);
        }
        String query;
        String param1 = "id=" + id;
        String param2 = "pid=" + 0;
        query = String.format("%s&%s", param1, param2);
        openConnection(query, url);
        if (br != null) {
            secret = br.readLine();
            NetworkLog.logMsg(NetworkLog.LOG_DEBUG, this, "Account secret key successfully acquired.");
            br.close();
        }
        return secret;
    }
}
