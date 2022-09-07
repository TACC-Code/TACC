class BackupThread extends Thread {
    public boolean connect(String _username, String password) {
        System.out.println("* * Connecting to server * *");
        XMPPConnection.DEBUG_ENABLED = true;
        ConnectionConfiguration config = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        config.setCompressionEnabled(true);
        config.setSASLAuthenticationEnabled(false);
        connection = new XMPPConnection(config);
        try {
            connection.connect();
        } catch (XMPPException ex) {
            JOptionPane.showMessageDialog(AccountsManagerFrame.getInstance(), "remote-server-timeout(504) Could not connect to talk.google.com:5222\n" + ex.getMessage(), "Server Not Responding", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            connection.login(_username, password, "CronyIM");
        } catch (XMPPException ex) {
            if (ex.getMessage().equals("forbidden(403) Username or password not correct.")) {
                JOptionPane.showMessageDialog(AccountsManagerFrame.getInstance(), "Invalid User Name or password", "Error Loggin in", JOptionPane.ERROR_MESSAGE);
                return false;
            } else System.out.println(ex);
        }
        chatManager = new GtalkChatManager(connection.getChatManager(), jid);
        connection.addPacketListener(chatManager, new MessageTypeFilter(Message.Type.chat));
        connection.addConnectionListener(new ServerConnectionListener());
        connection.addPacketListener(new GtalkPacketListener(), new PacketTypeFilter(IQ.class));
        roster = connection.getRoster();
        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
        if (connection.isConnected()) {
            System.out.println("* * Connected * *");
            jid = StringUtils.parseBareAddress(connection.getUser());
            username = StringUtils.parseName(jid);
            gService = new GtalkService(username);
            Thread gServiceThread = new Thread(gService, "Gtalk Service Thread");
            gServiceThread.start();
            return true;
        } else return false;
    }
}
