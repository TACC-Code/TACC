class BackupThread extends Thread {
    public Communicator(String serverAddress, Integer serverPort, String username, String password) throws XMPPException {
        username = canonicalizeUsername(username);
        SmackConfiguration.setPacketReplyTimeout(PACKET_REPLY_TIMEOUT);
        ConnectionConfiguration config = new ConnectionConfiguration(serverAddress, serverPort != null ? serverPort : DEFAULT_XMPP_SERVER_PORT, XMPP_HOST_NAME, ProxyInfo.forDefaultProxy());
        conn = new XMPPConnection(config);
        conn.connect();
        System.out.println("Connected to " + serverAddress + ":" + serverPort);
        conn.login(username, password);
        System.out.println("Logged in as " + username);
        setStatus(true, "ON");
    }
}
