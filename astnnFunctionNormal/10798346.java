class BackupThread extends Thread {
    public void messageProcessed(AbstractIRCChannel chann, IRCMessage msg) {
        if (msg.getType().equals(IRCMessageTypes.MSG_JOIN)) {
            String name = msg.getNick();
            for (int i = 0; i < users.length; i++) if (users[i].equals(name)) return;
            users = chann.getUsers();
            for (int i = 0; i < users.length; i++) if (users[i].equals(name)) listModel.add(i, name);
            if (msg.getNick().equals(chann.getParentSession().getNickName())) {
                Object args[] = new Object[] { new Date() };
                String txt = App.localization.localize("app", "irc.msg_join.self", "Joined the channel on {0,date,full} at {0,time,full}", args);
                append(txt, "JOIN", false);
            } else {
                String args[] = new String[] { msg.getNick() };
                String txt = App.localization.localize("app", "irc.msg_join", "{0} has joined the channel", args);
                append(txt, "JOIN", false);
            }
        } else if (msg.getType().equals(IRCMessageTypes.MSG_KICK)) {
            String name = KickMessage.getUser(msg);
            for (int i = 0; i < users.length; i++) if (IRCUtils.stripNickStatus(users[i]).equals(name)) listModel.remove(i);
            users = chann.getUsers();
            String comment = KickMessage.getComment(msg);
            String txt;
            if (comment.equals("") || comment.equals(msg.getNick())) {
                String args[] = new String[] { name, msg.getNick() };
                txt = App.localization.localize("app", "irc.msg_kick.nocomment", "{0} has been kicked by {1}", args);
            } else {
                String args[] = new String[] { name, msg.getNick(), comment };
                txt = App.localization.localize("app", "irc.msg_kick.comment", "{0} has been kicked by {1} ({2})", args);
            }
            append(txt, "KICK", false);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_MODE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_NICK)) {
            for (int i = 0; i < users.length; i++) if (IRCUtils.stripNickStatus(users[i]).equals(msg.getNick())) {
                listModel.remove(i);
                break;
            }
            users = chann.getUsers();
            for (int i = 0; i < users.length; i++) if (IRCUtils.stripNickStatus(users[i]).equals(NickMessage.getNickname(msg))) {
                listModel.add(i, users[i]);
                break;
            }
            String args[] = new String[] { msg.getNick(), NickMessage.getNickname(msg) };
            String txt = App.localization.localize("app", "irc.msg_nick", "{0} changed his nick to {1}", args);
            append(txt, "NICK", false);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_NOTICE)) {
            String args[] = new String[] { msg.getNick(), NoticeMessage.getText(msg) };
            String txt = App.localization.localize("app", "irc.msg_notice", "{0}: {1}", args);
            append(txt, "NOTICE", false);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PART)) {
            if (msg.getNick().equals(chann.getParentSession().getNickName())) {
                Object args[] = new Object[] { new Date() };
                String txt = App.localization.localize("app", "irc.msg_part.self", "Left the channel on {0,date,full} at {0,time,full}", args);
                append(txt, "PART", false);
            }
            String name = msg.getNick();
            for (int i = 0; i < users.length; i++) if (IRCUtils.stripNickStatus(users[i]).equals(name)) listModel.remove(i);
            users = chann.getUsers();
            String txt = PartMessage.getMessage(msg);
            if (txt.equals("")) txt = App.localization.localize("app", "irc.msg_part.nopartmessage", "No part message");
            String args[] = new String[] { msg.getNick(), txt };
            txt = App.localization.localize("app", "irc.msg_part", "{0} has left the channel ({1})", args);
            append(txt, "PART", false);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PRIVMSG)) {
            if (CTCPMessage.isCTCPMessage(PrivateMessage.getText(msg))) {
                CTCPMessage ctcp = CTCPMessage.parseMessageString(PrivateMessage.getText(msg));
                if (ctcp.getType().equals(CTCPMessageTypes.CTCP_ACTION)) {
                    String args[] = new String[] { msg.getNick(), ActionMessage.getText(ctcp) };
                    String txt = App.localization.localize("app", "irc.msg_privmsg.ctcp_action", "{0} {1}", args);
                    boolean flag = (ActionMessage.getText(ctcp).indexOf(chann.getParentSession().getNickName()) != -1);
                    append(txt, "ACTION", flag);
                }
            } else {
                String args[] = new String[] { msg.getNick(), PrivateMessage.getText(msg) };
                String txt = App.localization.localize("app", "irc.msg_privmsg", "<{0}> {1}", args);
                boolean flag = (PrivateMessage.getText(msg).indexOf(chann.getParentSession().getNickName()) != -1);
                append(txt, "PRIVMSG", flag);
            }
        } else if (msg.getType().equals(IRCMessageTypes.MSG_QUIT)) {
            if (msg.getNick().equals(chann.getParentSession().getNickName())) {
                Object args[] = new Object[] { new Date() };
                String txt = App.localization.localize("app", "irc.msg_quit.self", "Left IRC on {0,date,full} at {0,time,full}", args);
                append(txt, "QUIT", false);
                panelContainer.removeDockingPanel(this);
                return;
            }
            String name = msg.getNick();
            for (int i = 0; i < users.length; i++) if (IRCUtils.stripNickStatus(users[i]).equals(name)) listModel.remove(i);
            users = chann.getUsers();
            String txt = QuitMessage.getQuitMessage(msg);
            if (txt.equals("")) txt = App.localization.localize("app", "irc.msg_quit.noquitmessage", "No quit message");
            String args[] = new String[] { msg.getNick(), txt };
            txt = App.localization.localize("app", "irc.msg_quit", "{0} has quit IRC ({1})", args);
            append(txt, "QUIT", false);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_TOPIC)) {
            setDescription(IRCUtils.getDecodedString(TopicMessage.getTopic(msg)));
            String args[] = new String[] { chann.getTopic() };
            String txt = App.localization.localize("app", "irc.msg_topic", "The channel''s topic has been changed to: {0}", args);
            append(txt, "TOPIC", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHNICK)) {
            String args[] = new String[] { NosuchnickError.getNickname(msg) };
            String txt = App.localization.localize("app", "irc.err_nosuchnick", "No such nick: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHCHANNEL)) {
            String args[] = new String[] { NosuchchannelError.getChannelname(msg) };
            String txt = App.localization.localize("app", "irc.err_nosuchchannel", "No such channel: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_CANNOTSENDTOCHAN)) {
            String txt = App.localization.localize("app", "irc.err_cannotsendtochan", "The message couldn't be sent to the channel");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_AWAY)) {
            String args[] = new String[] { AwayReply.getNick(msg), AwayReply.getMessage(msg) };
            String txt = App.localization.localize("app", "irc.rpl_away", "{0} is away ({1})", args);
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_NOTOPIC)) {
            setDescription("");
            String txt = App.localization.localize("app", "irc.rpl_notopic", "Nobody dared to set a channel topic yet");
            append(txt, "TOPIC", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TOPIC)) {
            setDescription(IRCUtils.getDecodedString(TopicMessage.getTopic(msg)));
            String args[] = new String[] { chann.getTopic() };
            String txt = App.localization.localize("app", "irc.rpl_topic", "The channel''s topic is {0}", args);
            append(txt, "TOPIC", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TOPICSET)) {
            Object args[] = new Object[] { TopicsetReply.getUserName(msg), TopicsetReply.getTimecode(msg) };
            String txt = App.localization.localize("app", "irc.msg_topic", "The topic has been set by {0} on {1,date,EEE',' MMM d',' yyyy} at " + "{1,time,HH:mm:ss a}", args);
            append(txt, "TOPIC", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFNAMES)) {
            users = new String[0];
            listModel.clear();
            users = chann.getUsers();
            listModel.addAll(users);
        }
    }
}
