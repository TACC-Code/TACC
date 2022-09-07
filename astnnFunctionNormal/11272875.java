class BackupThread extends Thread {
    protected void processMessage(Message m, Session session) {
        String nickname = m.getDataString(0);
        Session targetSession = session.getServerFacilities().searchForSession(nickname);
        if (targetSession != null) {
            ChannelManager channelManager = session.getServerFacilities().getChannelManager();
            UserState userState = targetSession.getUserState();
            StringBuffer msg = new StringBuffer(160);
            StringBuffer channelNames = new StringBuffer(50);
            Channel[] channels = channelManager.getChannelsForUser(nickname);
            if (channels != null) {
                int numChannels = channels.length;
                boolean firstTime = true;
                for (int x = 0; x < numChannels; x++) {
                    if (!firstTime) {
                        channelNames.append(" ");
                    }
                    channelNames.append(channels[x].getName());
                    firstTime = false;
                }
            }
            msg.append(nickname);
            msg.append(" \"");
            if (DataDefinitions.UserLevelTypes.USER.equals(userState.getUser().getLevel())) {
                msg.append("User");
            } else {
                msg.append("Admin");
            }
            msg.append("\" ");
            msg.append(targetSession.getTimeConnected());
            msg.append(" ");
            msg.append("\"");
            msg.append(channelNames.toString());
            msg.append("\"");
            msg.append(" \"Active.\" ");
            msg.append(userState.getShareCount());
            msg.append(" ");
            msg.append(userState.getDownloadCount());
            msg.append(" ");
            msg.append(userState.getUploadCount());
            msg.append(" ");
            msg.append(userState.getLinkType());
            msg.append(" \"");
            msg.append(userState.getClientInfo());
            msg.append("\"");
            Message response = new Message(MessageTypes.SERVER_WHOIS_RESPONSE, msg.toString());
            OutboundMessageQueue out = session.getOutboundMessageQueue();
            try {
                out.queueMessage(response);
            } catch (InvalidatedQueueException iqe) {
            }
        } else {
            UserPersistenceStore store = session.getServerFacilities().getUserPersistenceStore();
            User u = null;
            try {
                u = store.retrieveUser(nickname);
            } catch (PersistenceException upe) {
                Logger.getInstance().log(Logger.SEVERE, "Persistence retrieve error in WhoisHandler: " + upe.toString());
            }
            if (u != null) {
                StringBuffer msg = new StringBuffer(30);
                msg.append(nickname);
                msg.append(" ");
                msg.append(u.getLevel());
                msg.append(" ");
                msg.append(u.getLastseen());
                Message response = new Message(MessageTypes.SERVER_WHOWAS_RESPONSE, msg.toString());
                OutboundMessageQueue out = session.getOutboundMessageQueue();
                try {
                    out.queueMessage(response);
                } catch (InvalidatedQueueException iqe) {
                }
            }
        }
    }
}
