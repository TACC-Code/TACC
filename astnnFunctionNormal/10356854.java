class BackupThread extends Thread {
    void connect() {
        if (!this.conf.get("XMPP.username").isEmpty()) {
            if (this.conf.get("XMPP.password").isEmpty()) {
                this.passwordDialog.setVisible(true);
            }
            connected();
            ConnectionConfiguration Configuration = new ConnectionConfiguration(this.conf.get("XMPP.server"), Integer.valueOf(this.conf.get("XMPP.port")));
            this.Connection = new XMPPConnection(Configuration);
            try {
                ServiceDiscoveryManager.setIdentityName("Mapadeo");
                ServiceDiscoveryManager.setIdentityType("pc");
                this.Connection.connect();
                SDM = ServiceDiscoveryManager.getInstanceFor(this.Connection);
                SDM.addFeature("http://mapadeo.zestflood.net/1.0/xhtml");
                this.ConnectionStateLabel.setText("authenticating");
                if (this.conf.get("XMPP.password").isEmpty()) {
                    this.Connection.login(this.conf.get("XMPP.username"), this.passwordTextField.getText(), this.conf.get("XMPP.location"));
                } else {
                    this.Connection.login(this.conf.get("XMPP.username"), this.conf.get("XMPP.password"), this.conf.get("XMPP.location"));
                }
                this.ConnectionStateLabel.setText("connected");
                if (this.Connection.isUsingTLS()) {
                    this.ConnectionStateLabel.setText("connected [TLS]");
                } else {
                    this.ConnectionStateLabel.setText("connected");
                }
                this.MUChats = new Vector<ChatInstanceHandlerMUC>();
                this.Connection.addConnectionListener(this);
                this.IQListener = new XMLIQExtensionListener(this.Connection, this);
                this.Connection.addPacketListener(IQListener, new PacketTypeFilter(XMLIQExtension.class));
                this.RosterListModel.clear();
                this.RosterMan = new RosterManager(this.Connection.getRoster(), this.RosterListModel);
                this.ChatListener = new ChatListener(this, Connection);
                this.Connection.getChatManager().addChatListener(this.ChatListener);
                MultiUserChat.addInvitationListener(Connection, ChatListener);
            } catch (XMPPException e) {
                this.ConnectionStateLabel.setText(e.getMessage());
                this.Connection.disconnect();
                disconnected();
            }
            this.passwordTextField.setText("");
        }
    }
}
