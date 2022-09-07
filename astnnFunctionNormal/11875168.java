class BackupThread extends Thread {
    public void messageReceived(String prefix, String command, String params[]) {
        Object[] b = _messagelisteners.sendEvent("messageReceived", new Object[] { prefix, command, params, this });
        for (int i = 0; i < b.length; i++) if (((Boolean) b[i]).booleanValue()) return;
        String toSend = "";
        for (int i = 0; i < params.length; i++) toSend += " " + params[i];
        command = command.toLowerCase(java.util.Locale.ENGLISH);
        String nick = extractNick(prefix);
        if (command.equals("notice")) {
            if (!ignore(nick)) {
                if (!_filter.performFromNotice(nick, params[1])) if (_defaultSource != null) _defaultSource.noticeReceived(nick, params[1]);
            }
        } else if (command.equals("privmsg")) {
            if (!ignore(nick)) {
                if (isChannel(params[0])) {
                    if (!_filter.performFromChannelMessage(params[0], nick, params[1])) {
                        Channel c = getChannel(params[0], false);
                        if (c != null) c.messageReceived(nick, params[1]);
                    }
                } else {
                    if (!_filter.performFromNickMessage(nick, params[1])) {
                        Query q = getQuery(nick, false);
                        if (q != null) q.messageReceived(nick, params[1]);
                    }
                }
            }
        } else if (command.equals("join")) {
            if (!nick.equals(getNick())) {
                Channel c = getChannel(params[0], false);
                if (c != null) c.joinNick(nick, "");
            } else {
                Channel c = getChannel(params[0], true);
                if (c != null) {
                    c.resetNicks();
                    execute("mode " + params[0]);
                }
            }
        } else if (command.equals("part")) {
            Channel c = getChannel(params[0], false);
            if (c != null) {
                if (params.length > 1) {
                    c.partNick(nick, params[1]);
                } else {
                    c.partNick(nick, "");
                }
                if (nick.equals(getNick())) {
                    _listeners.sendEvent("sourceRemoved", c, this);
                    deleteChannel(c.getName());
                }
            }
        } else if (command.equals("kick")) {
            Channel c = getChannel(params[0], false);
            if (c != null) {
                String target = params[1];
                String reason = "";
                if (params.length > 2) reason = params[2];
                c.kickNick(target, nick, reason);
                if (target.equals(getNick())) {
                    if (_ircConfiguration.getB("autorejoin")) {
                        c.report("Attempting to rejoin room " + c.getName() + "...");
                        execute("join " + params[0]);
                    } else {
                    }
                }
            }
        } else if (command.equals("topic")) {
            Channel c = getChannel(params[0], false);
            if (c != null) c.setTopic(params[1], nick);
        } else if (command.equals("mode")) {
            String full = "";
            for (int i = 1; i < params.length; i++) full += params[i] + " ";
            if (isChannel(params[0])) {
                Channel c = getChannel(params[0], false);
                if (c != null) {
                    MultiModeHandler h = new MultiModeHandler(full, _globalModes, _nickModes);
                    while (!h.terminated()) {
                        h.next();
                        if (h.isPrefix() || h.isModeA()) {
                            c.applyUserMode(h.getParameter(), h.getMode(), nick);
                        } else {
                            if (h.hasParameter()) c.applyMode(h.getMode() + " " + h.getParameter(), nick); else c.applyMode(h.getMode(), nick);
                        }
                    }
                }
            } else if (nick.equals(getNick())) {
                _mode.apply(full);
                if (_status != null) _status.modeChanged(getMode());
            }
        } else if (command.equals("nick")) {
            if (nick.equals(getNick())) {
                _nick = params[0];
                if (_status != null) _status.nickChanged(getNick());
            }
            globalNickChange(nick, params[0]);
        } else if (command.equals("quit")) {
            if (params.length > 0) globalNickRemove(nick, params[0]); else globalNickRemove(nick, "");
        } else if (command.equals("ping")) {
            execute("pong :" + params[0]);
        } else if (command.equals("invite")) {
            String invited = params[0];
            String channel = params[1];
            if (invited.equals(getNick())) {
                if (_defaultSource != null) _defaultSource.report("3      *** " + nick + " has invited you to join " + channel + ". Type /join " + channel + " to accept this invitation.");
            }
        } else if (command.equals("error")) {
        } else {
        }
    }
}
