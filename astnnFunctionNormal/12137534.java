class BackupThread extends Thread {
    public void statusChanged(IRCConnection conn, SessionStatus oldStatus) {
        if (conn.getStatus() == SessionStatus.AUTHENTICATING) {
            if (!getPassword().equals("")) {
                conn.send(PassMessage.createMessage("", "", "", getPassword()));
            }
            conn.send(NickMessage.createMessage("", "", "", createNick()));
            conn.send(UserMessage.createMessage("", "", "", loginUserName, "0", realName));
        } else if (conn.getStatus() == SessionStatus.CONNECTED) {
            AbstractIRCChannel[] chans = getChannels();
            IRCMessage msg;
            for (int i = 0; i < chans.length; i++) {
                if (IRCUtils.isChannel(chans[i].getName())) {
                    msg = JoinMessage.createMessage("", "", "", chans[i].getName());
                    conn.send(msg);
                }
            }
        } else if (conn.getStatus() == SessionStatus.UNCONNECTED) {
            AbstractIRCChannel[] chans = getChannels();
            for (int i = 0; i < chans.length; i++) {
                removeChannel(chans[i].getName());
            }
            if (!reconnect) {
                App.sessions.removeSession(this);
            }
        }
        fireStatusChangedEvent(this, oldStatus);
        if (reconnect) {
            reconnect();
        }
    }
}
