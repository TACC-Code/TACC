class BackupThread extends Thread {
    public Connection(String host, int port, String username, String password, boolean newAccount) {
        try {
            this.config = new ConnectionConfiguration(host, port);
            this.config.setSASLAuthenticationEnabled(true);
            this.connection = new XMPPConnection(this.config);
            this.connection.connect();
            this.user = username;
            if (newAccount) {
                this.connection.getAccountManager().createAccount(username, password);
                this.connection.login(username, password, "jiaho");
                this.connection.addPacketListener(this, new MessageTypeFilter(Message.Type.chat));
                this.connection.getRoster().addRosterListener(this);
                this.connection.addConnectionListener(this);
                this.isLogin = true;
                this.isClose = false;
            } else {
                this.connection.login(username, password, "jiaho");
                this.connection.addPacketListener(this, new MessageTypeFilter(Message.Type.chat));
                this.connection.getRoster().addRosterListener(this);
                this.connection.addConnectionListener(this);
                this.isLogin = true;
                this.isClose = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
