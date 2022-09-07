class BackupThread extends Thread {
    private void connect() {
        ConnectionConfiguration config = new ConnectionConfiguration("jabber.ccc.de", 5222);
        config.setSASLAuthenticationEnabled(true);
        connection = new XMPPConnection(config);
        try {
            System.out.println("Connecting…");
            connection.connect();
            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            System.out.println("Logging in...");
            connection.login("david.strohmayer", "ramodroll", "ECLIPSE");
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
}
