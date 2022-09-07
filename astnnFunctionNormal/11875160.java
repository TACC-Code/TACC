class BackupThread extends Thread {
    public void replyReceived(String prefix, String id, String params[]) {
        Object[] b = _replylisteners.sendEvent("replyReceived", new Object[] { prefix, id, params, this });
        for (int i = 0; i < b.length; i++) if (((Boolean) b[i]).booleanValue()) return;
        if (id.equals("232")) {
            String toSend = "";
            for (int i = 1; i < params.length; i++) toSend += " " + params[i];
            toSend = toSend.substring(1);
            sendStatusMessage(toSend);
        } else if (id.equals("324")) {
            Channel c = getChannel(params[1], false);
            if (c != null) {
                String mode = "";
                for (int i = 2; i < params.length; i++) mode += " " + params[i];
                mode = mode.substring(1);
                c.applyMode(mode, "");
            }
        } else if (id.equals("332")) {
            Channel c = getChannel(params[1], false);
            if (c != null) c.setTopic(params[2], "");
        } else if (id.equals("353")) {
            int first = 1;
            if (params[1].length() == 1) first++;
            Channel c = getChannel(params[first], false);
            if (c != null) {
                String nick = "";
                Vector nicks = new Vector();
                for (int i = 0; i < params[first + 1].length(); i++) {
                    char u = params[first + 1].charAt(i);
                    if (u == ' ') {
                        if (nick.length() > 0) nicks.insertElementAt(nick, nicks.size());
                        nick = "";
                    } else {
                        nick += u;
                    }
                }
                if (nick.length() > 0) nicks.insertElementAt(nick, nicks.size());
                setNicks(c, nicks);
            }
        } else if (id.equals("001")) {
            String nick = params[0];
            if (!(nick.equals(_nick))) {
                _nick = nick;
                if (_status != null) _status.nickChanged(nick);
            }
            _connected = true;
            _listeners.sendEvent("serverConnected", this);
        } else if (id.equals("005")) {
            learnServerVariables(params);
        } else if (id.equals("321")) {
            getChanList(_host[_tryServerIndex]).begin();
        } else if (id.equals("322")) {
            String name = params[1];
            int count = new Integer(params[2]).intValue();
            if ((count < 32767) && (isChannel(name))) {
                String topic = params[3];
                getChanList(_host[_tryServerIndex]).addChannel(new ChannelInfo(name, topic, count));
            }
        } else if (id.equals("323")) {
            getChanList(_host[_tryServerIndex]).end();
        } else if (id.equals("433")) {
            if (!_connected) {
                nickUsed();
                register();
            } else if (_defaultSource != null) _defaultSource.report("3      *** That nickname is already in use, please try another one.");
        } else if (id.equals("400")) {
            if (_defaultSource != null) _defaultSource.report("2      *** The message could not be sent.");
        } else if (id.equals("401")) {
            if (_defaultSource != null) _defaultSource.report("3      *** That room or person does not exist.");
        } else if (id.equals("402")) {
            if (_defaultSource != null) _defaultSource.report("3      *** The server you specified does not exist.");
        } else if (id.equals("403")) {
            if (_defaultSource != null) _defaultSource.report("3      *** The room you are trying to set does not exist");
        } else if (id.equals("404")) {
            if (_defaultSource != null) _defaultSource.report("2      *** The message could not be sent. The message was either blocked or you may not be allowed to communicate in this room.");
        } else if (id.equals("405")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Unable to join because you have too many rooms open.");
        } else if (id.equals("412")) {
            if (_defaultSource != null) _defaultSource.report("\2\33      *** You must first type a message before sending it.");
        } else if (id.equals("421")) {
            if (_defaultSource != null) _defaultSource.report("\2\33      *** The command you typed is not supported.");
        } else if (id.equals("432")) {
            if (_defaultSource != null) _defaultSource.report("\2\33      *** That nickname is not allowed. Please choose another nickname.");
        } else if (id.equals("433")) {
            if (_defaultSource != null) _defaultSource.report("\2\33      *** That nickname is already in use, please try another one.");
        } else if (id.equals("438")) {
            if (_defaultSource != null) _defaultSource.report("\2\33      *** You are trying to change your nickname too quickly, try again later.");
        } else if (id.equals("440")) {
            if (_defaultSource != null) _defaultSource.report("\2\33      *** Services are currently down. Please try again later.");
        } else if (id.equals("442")) {
            Channel chan = getChannel(params[1], false);
            if (chan != null) {
                _listeners.sendEvent("sourceRemoved", chan, this);
                deleteChannel(chan.getName());
            } else if (_defaultSource != null) _defaultSource.report("\2\33      *** You must first join the room before doing that.");
        } else if (id.equals("443")) {
            String cname = params[1];
            if (_defaultSource != null) _defaultSource.report("\2\33      *** Your invitation to " + cname + " could not be sent because that user is already in the room.");
        } else if (id.equals("460")) {
            if (_defaultSource != null) _defaultSource.report("\2\34      *** You don't have permission to do that in this chat room.");
        } else if (id.equals("461")) {
            if (_defaultSource != null) _defaultSource.report("2      *** You must specify more parameters for this command.");
        } else if (id.equals("462")) {
            if (_defaultSource != null) _defaultSource.report("4      *** You have already registered, you may not reregister.");
        } else if (id.equals("463")) {
            if (_defaultSource != null) _defaultSource.report("4      *** You are not among the privileged.");
        } else if (id.equals("464")) {
            if (_defaultSource != null) _defaultSource.report("4      *** Your authentication password has failed.");
        } else if (id.equals("465")) {
            if (_defaultSource != null) _defaultSource.report("4      *** You are currently banned completely from the FrostWire Chat service due to violations of the Code of Conduct. This ban is permanent.");
        } else if (id.equals("447")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Nickname changes are not permitted at this time, try again later.");
        } else if (id.equals("467")) {
            if (_defaultSource != null) _defaultSource.report("3      *** The room key has already been set.");
        } else if (id.equals("468")) {
            if (_defaultSource != null) _defaultSource.report("3      *** You must be a Sysop to change that mode.");
        } else if (id.equals("469")) {
            if (_defaultSource != null) _defaultSource.report("3      *** The link is already set for this room.");
        } else if (id.equals("470")) {
            if (_defaultSource != null) _defaultSource.report("3      *** The room you are trying to join is full. Trying next available room...");
        } else if (id.equals("471")) {
            if (_defaultSource != null) _defaultSource.report("3      *** The room you are trying to join is full. You cannot join it.");
        } else if (id.equals("472")) {
            if (_defaultSource != null) _defaultSource.report("3      *** The mode you are trying to set does not exist.");
        } else if (id.equals("473")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Only those who have been invited may enter that room.");
        } else if (id.equals("474")) {
            if (_defaultSource != null) _defaultSource.report("4      *** You are currently banned by a host in that chat room due to violations of the Code of Conduct. You may be able to rejoin at a later time.");
        } else if (id.equals("475")) {
            if (_defaultSource != null) _defaultSource.report("3      *** The room you are trying to join is protected with a password. You cannot join it.");
        } else if (id.equals("476")) {
            if (_defaultSource != null) _defaultSource.report("3      *** The room mode you are trying to set does not exist");
        } else if (id.equals("477")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Only authenticated users can join that room.");
        } else if (id.equals("478")) {
            if (_defaultSource != null) _defaultSource.report("3      *** The ban list is full. You must remove a current ban before setting a new one.");
        } else if (id.equals("479")) {
            if (_defaultSource != null) _defaultSource.report("3      *** The room you are trying to join has an invalid link set and can't be joined.");
        } else if (id.equals("480")) {
            String cname = params[1];
            if (_defaultSource != null) _defaultSource.report("3      *** " + cname + " does not allow knocks.");
        } else if (id.equals("481")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Permission Denied. You are not a System Operator.");
        } else if (id.equals("482")) {
            if (_defaultSource != null) _defaultSource.report("3      *** You don't have permission to do that in this chat room.");
        } else if (id.equals("483")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Permission Denied. You are not a System Operator.");
        } else if (id.equals("484")) {
            if (_defaultSource != null) _defaultSource.report("3      *** The user is protected. You don't have permission to do that on this network.");
        } else if (id.equals("485")) {
            if (_defaultSource != null) _defaultSource.report("3      *** You don't have permission to do that on this network.");
        } else if (id.equals("486")) {
            if (_defaultSource != null) _defaultSource.report("3      *** You must be authenticated to message that user.");
        } else if (id.equals("487")) {
            if (_defaultSource != null) _defaultSource.report("3      *** You must be a System Operator to execute that command.");
        } else if (id.equals("488")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Waiting: This option is temporarily disabled.");
        } else if (id.equals("489")) {
            if (_defaultSource != null) _defaultSource.report("3      *** You must be using a secure connection to join that room.");
        } else if (id.equals("490")) {
            if (_defaultSource != null) _defaultSource.report("3      *** This user has blocked private messages that are not suitable for all audiences.");
        } else if (id.equals("491")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Permission Denied. You are not authorized to access network controls.");
        } else if (id.equals("492")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Your request could not be sent because that user does not allow it.");
        } else if (id.equals("499")) {
            if (_defaultSource != null) _defaultSource.report("3      *** You must be an Owner in the chat room to complete the action you just attempted.");
        } else if (id.equals("500")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Too many join requests. Please wait a while and try again.");
        } else if (id.equals("501")) {
            if (_defaultSource != null) _defaultSource.report("2      *** Unknown user mode.");
        } else if (id.equals("502")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Permission Denied. You are not a System Operator.");
        } else if (id.equals("511")) {
            if (_defaultSource != null) _defaultSource.report("2      *** You have reached the maximum amount of entries allowed in your silence list.");
        } else if (id.equals("512")) {
            if (_defaultSource != null) _defaultSource.report("2      *** You have reached the maximum amount of entries allowed in your watch list.");
        } else if (id.equals("513")) {
            if (_defaultSource != null) _defaultSource.report("2      *** To connect, type /QOUTE PONG %lX");
        } else if (id.equals("514")) {
            if (_defaultSource != null) _defaultSource.report("2      *** You have reached the maximum amount of entries allowed in your DCC list.");
        } else if (id.equals("518")) {
            if (_defaultSource != null) _defaultSource.report("3      *** This room does not allow invitations.");
        } else if (id.equals("519")) {
            if (_defaultSource != null) _defaultSource.report("3      *** You must be a System Operator to join that room.");
        } else if (id.equals("520")) {
            if (_defaultSource != null) _defaultSource.report("3      *** You must be an Operator to join that room.");
        } else if (id.equals("524")) {
            if (_defaultSource != null) _defaultSource.report("3      *** You must be a System Operator to join that room.");
        } else if (id.equals("972")) {
            if (_defaultSource != null) _defaultSource.report("3      *** You don't have permission to do that in this chat room.");
        } else if (id.equals("974")) {
            if (_defaultSource != null) _defaultSource.report("3      *** You don't have permission to do that in this chat room.");
        } else if (id.equals("951")) {
            if (_defaultSource != null) _defaultSource.report("3      *** The server is too busy to process your authentication at this time.");
        } else if (id.equals("952")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Username and/or password do not match.");
        } else if (id.equals("953")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Username and/or password do not match.");
        } else if (id.equals("954")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Username and/or password do not match.");
        } else if (id.equals("955")) {
            if (_defaultSource != null) _defaultSource.report("3      *** Username and/or password do not match.");
        } else {
        }
    }
}
