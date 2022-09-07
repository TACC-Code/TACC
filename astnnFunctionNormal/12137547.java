class BackupThread extends Thread {
    protected void setupMessageMap() {
        this.addMessageHandler(IRCMessageTypes.MSG_JOIN, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                ArrayList chans = JoinMessage.getChannels(e.getMessage());
                for (int i = 0; i < chans.size(); i++) {
                    forwardMessage(e, (String) chans.get(i));
                }
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
        });
        this.addMessageHandler(IRCMessageTypes.MSG_KICK, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                forwardMessage(e, KickMessage.getChannel(e.getMessage()));
                if (KickMessage.getUser(e.getMessage()).equals(user.getNickName())) {
                    removeChannel(KickMessage.getChannel(e.getMessage()));
                }
            }
        });
        this.addMessageHandler(IRCMessageTypes.MSG_NICK, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                if (e.getMessage().getNick().equals(user.getNickName())) {
                    user.setNickName(NickMessage.getNickname(e.getMessage()));
                }
                forwardMessage(e, null);
            }
        });
        this.addMessageHandler(IRCMessageTypes.MSG_NOTICE, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                MessageFormat f = new MessageFormat("[{0}] {1}");
                try {
                    Object[] o = f.parse(NoticeMessage.getText(e.getMessage()));
                    if (IRCUtils.isChannel(o[0].toString())) {
                        forwardMessage(e, o[0].toString());
                        e.consume();
                        fireMessageProcessedEvent(e.getMessage());
                        return;
                    }
                } catch (ParseException exc) {
                }
                if (e.getMessage().getNick().equals("ChanServ") || e.getMessage().getNick().equals("NickServ")) {
                    forwardMessage(e, e.getMessage().getNick());
                    e.consume();
                }
            }
        });
        this.addMessageHandler(IRCMessageTypes.MSG_PART, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                ArrayList chans = PartMessage.getChannels(e.getMessage());
                for (int i = 0; i < chans.size(); i++) {
                    forwardMessage(e, (String) chans.get(i));
                    if (e.getMessage().getNick().equals(user.getNickName())) {
                        removeChannel((String) chans.get(i));
                    }
                }
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
        });
        this.addMessageHandler(IRCMessageTypes.MSG_PING, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                conn.send(PongMessage.createMessage("", "", "", PingMessage.getServer1(e.getMessage())));
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
        });
        this.addMessageHandler(IRCMessageTypes.MSG_PRIVMSG, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                if (IRCUtils.isChannel(PrivateMessage.getMsgtarget(e.getMessage()))) {
                    forwardMessage(e, PrivateMessage.getMsgtarget(e.getMessage()));
                } else {
                    if (CTCPMessage.isCTCPMessage(PrivateMessage.getText(e.getMessage()))) {
                        return;
                    } else {
                        forwardMessage(e, e.getMessage().getNick());
                    }
                }
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
        });
        this.addMessageHandler(IRCMessageTypes.MSG_QUIT, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                forwardMessage(e, null);
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
        });
        this.addMessageHandler(IRCMessageTypes.MSG_TOPIC, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                forwardMessage(e, TopicMessage.getChannel(e.getMessage()));
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
        });
        this.addMessageHandler(IRCMessageTypes.ERR_NOSUCHNICK, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                String channel = NosuchnickError.getNickname(e.getMessage());
                if (getChannel(channel) != null) {
                    forwardMessage(e, channel);
                    e.consume();
                    fireMessageProcessedEvent(e.getMessage());
                }
            }
        });
        this.addMessageHandler(IRCMessageTypes.ERR_NOSUCHCHANNEL, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                String channel = NosuchchannelError.getChannelname(e.getMessage());
                if (getChannel(channel) != null) {
                    forwardMessage(e, channel);
                    e.consume();
                    fireMessageProcessedEvent(e.getMessage());
                }
            }
        });
        this.addMessageHandler(IRCMessageTypes.ERR_NICKNAMEINUSE, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                if (isChangeingNick() == false) {
                    disconnect();
                    e.consume();
                    fireMessageProcessedEvent(e.getMessage());
                    reconnect = true;
                }
            }
        });
        this.addMessageHandler(IRCMessageTypes.ERR_NICKCOLLISION, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                disconnect();
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
                reconnect = true;
            }
        });
        this.addMessageHandler(IRCMessageTypes.RPL_WELCOME, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                String str = WelcomeReply.getNick(e.getMessage());
                if (user.getNickName().startsWith(str)) {
                    user.setNickName(str);
                }
                if (conn.isActive()) {
                    conn.setStatus(SessionStatus.CONNECTED);
                }
            }
        });
        this.addMessageHandler(IRCMessageTypes.RPL_AWAY, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                forwardMessage(e, AwayReply.getNick(e.getMessage()));
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
        });
        this.addMessageHandler(IRCMessageTypes.RPL_TOPIC, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                forwardMessage(e, TopicReply.getChannel(e.getMessage()));
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
        });
        this.addMessageHandler(IRCMessageTypes.RPL_TOPICSET, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                forwardMessage(e, TopicsetReply.getChannel(e.getMessage()));
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
        });
        this.addMessageHandler(IRCMessageTypes.RPL_NAMREPLY, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                String channel = NamesReply.getChannel(e.getMessage());
                if (getChannel(channel) != null) {
                    forwardMessage(e, channel);
                    e.consume();
                    fireMessageProcessedEvent(e.getMessage());
                }
            }
        });
        this.addMessageHandler(IRCMessageTypes.RPL_ENDOFNAMES, new IRCMessageHandler() {

            public void handleMessage(IRCMessageEvent e) {
                String channel = EndofnamesReply.getChannel(e.getMessage());
                if (getChannel(channel) != null) {
                    forwardMessage(e, channel);
                    e.consume();
                    fireMessageProcessedEvent(e.getMessage());
                }
            }
        });
    }
}
