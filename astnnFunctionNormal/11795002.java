class BackupThread extends Thread {
    private void actionPerformedInvite(InviteEvent event) throws FormatterException {
        String nick = event.getIrcNick();
        ArrayList<String> channels = new ArrayList<String>();
        ReplacerEnvironment env = new ReplacerEnvironment(GLOBAL_ENV);
        env.add("user", event.getUser());
        env.add("nick", event.getIrcNick());
        if ("INVITE".equals(event.getCommand()) || "SITE INVITE".equals(event.getCommand())) {
            ReplacerFormat format = ReplacerUtils.finalFormat(SiteBot.class, "invite.success");
            logger.info("Invited " + nick);
            for (Enumeration e = getIRCConnection().getClientState().getChannels(); e.hasMoreElements(); ) {
                Channel chan = (Channel) e.nextElement();
                Member m = chan.findMember(getIRCConnection().getClientState().getNick().getNick());
                if (m == null) m = chan.findMember("~" + getIRCConnection().getClientState().getNick().getNick());
                if (m == null) m = chan.findMember("%" + getIRCConnection().getClientState().getNick().getNick());
                if (m != null && m.hasOps()) {
                    ChannelConfig cc = _channelMap.get(chan.getName());
                    if (cc != null) {
                        if (cc.checkPerms(event.getUser())) {
                            _conn.sendCommand(new InviteCommand(nick, chan.getName()));
                            channels.add(chan.getName());
                            try {
                                notice(nick, "Channel key for " + chan.getName() + " is " + cc.getChannelKey(event.getUser()));
                            } catch (ObjectNotFoundException execption) {
                            }
                        } else {
                            logger.warn("User does not have enough permissions to invite into " + chan.getName());
                        }
                    } else {
                        logger.error("Could not find ChannelConfig for " + chan.getName() + " this is a bug, please report it!", new Throwable());
                    }
                }
            }
            sayEvent("invite", SimplePrintf.jprintf(format, env), channels);
            synchronized (_identWhoisList) {
                _identWhoisList.add(new WhoisEntry(nick, event.getUser()));
            }
            logger.info("Looking up " + nick + " to set IRCIdent");
            _conn.sendCommand(new WhoisCommand(nick));
        } else if ("BINVITE".equals(event.getCommand())) {
            ReplacerFormat format = ReplacerUtils.finalFormat(SiteBot.class, "invite.failed");
            sayEvent("invite", SimplePrintf.jprintf(format, env));
        }
    }
}
