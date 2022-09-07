class BackupThread extends Thread {
    public static void makeConnection(JTextField name, JTextField pass, JTextField server) throws XMPPException {
        user = new User(name.getText(), pass.getText(), "", "");
        ConnectionConfiguration config = new ConnectionConfiguration(server.getText(), PORT);
        connection = new XMPPConnection(config);
        try {
            connection.connect();
            if (connection.isConnected()) {
                SASLAuthentication.supportSASLMechanism("PLAIN", 0);
                connection.login(name.getText(), pass.getText(), "");
                Logger.getLogger(ClientConnection.class.getName()).log(Level.INFO, "I'm connected!");
            }
        } catch (XMPPException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
