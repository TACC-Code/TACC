class BackupThread extends Thread {
    public void run() {
        ConnectionConfiguration connConfig = new ConnectionConfiguration(this.host, 5222, this.domain);
        XMPPConnection connection = new XMPPConnection(connConfig);
        try {
            connection.connect();
            connection.login(this.loginname, this.password);
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
            ChatManager chatmanager = connection.getChatManager();
            Chat chat = chatmanager.createChat(this.peer, new imbotlistener());
            while (connection.isConnected()) {
                try {
                    if (!this.messagequeue.empty()) {
                        String newmessage = this.messagequeue.pop();
                        Message msg = new Message(this.peer, Message.Type.chat);
                        msg.setBody(newmessage);
                        chat.sendMessage(msg);
                    }
                } catch (XMPPException e) {
                }
                try {
                    if (this.messagequeue.empty()) Thread.sleep(4000);
                    Thread.sleep(1000);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        } catch (XMPPException e) {
            try {
                Thread.sleep(10000);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            this.run();
        }
    }
}
