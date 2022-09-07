class BackupThread extends Thread {
    public List<Object[]> freeProxyConnection(String scriptName, String scriptMD5) {
        NetworkLog.logMsg(NetworkLog.LOG_INFO, this, "Starting a free proxy connection");
        rendezvousList = new RendezvousRelayList();
        XMLConfigParser.readUrlHost();
        remote_url = XMLConfigParser.urlHost + remotePath + scriptName + ".xml";
        local_url = localPath + scriptName + ".xml";
        if (!XMLConfigParser.getLanChoice() && XMLConfigParser.getInternetChoice()) {
            try {
                queryInactive();
                queryFile(scriptName, scriptMD5);
                url = new URL(remote_url);
                URLConnection urlConn;
                urlConn = url.openConnection();
                downloader(urlConn, scriptName);
                rendezvousList = XMLConfigParser.readRendezvouslist(local_url, rendezvousList);
            } catch (IOException e) {
                NetworkLog.logMsg(NetworkLog.LOG_ERROR, this, e.getMessage());
            }
        }
        shuffledRendezvousList = rendezvousList.shuffle();
        return shuffledRendezvousList;
    }
}
