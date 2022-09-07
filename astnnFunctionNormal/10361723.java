class BackupThread extends Thread {
    public List<Object[]> proxyConnection(String scriptName, String scriptMD5) {
        NetworkLog.logMsg(NetworkLog.LOG_INFO, this, "Starting a proxy connection");
        rendezvousList = new RendezvousRelayList();
        XMLConfigParser.readUrlHost();
        remote_url = XMLConfigParser.urlHost + remotePath + scriptName + ".xml";
        local_url = localPath + scriptName + ".xml";
        try {
            if (XMLConfigParser.getInternetChoice() && !XMLConfigParser.getLanChoice()) {
                XMLConfigParser.readProxyConfiguration();
                System.getProperties().put("proxySet", "true");
                System.getProperties().put("proxyHost", XMLConfigParser.proxyHost);
                System.getProperties().put("proxyPort", XMLConfigParser.proxyPort);
                queryInactive();
                queryFile(scriptName, scriptMD5);
                URL url = new URL(remote_url);
                URLConnection urlConn = url.openConnection();
                String password = XMLConfigParser.proxyUsername + ":" + XMLConfigParser.proxyPassword;
                String encoded = Base64.encodeBase64String(password.getBytes());
                urlConn.setRequestProperty("Proxy-Authorization", encoded);
                downloader(urlConn, scriptName);
                rendezvousList = XMLConfigParser.readRendezvouslist(local_url, rendezvousList);
            }
        } catch (Exception e) {
            NetworkLog.logMsg(NetworkLog.LOG_ERROR, this, e.getMessage());
        }
        shuffledRendezvousList = rendezvousList.shuffle();
        return shuffledRendezvousList;
    }
}
