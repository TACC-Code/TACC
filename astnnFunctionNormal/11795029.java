class BackupThread extends Thread {
    protected void reload(Properties ircCfg) throws IOException {
        _server = PropertyHelper.getProperty(ircCfg, "irc.server", null);
        _port = Integer.parseInt(PropertyHelper.getProperty(ircCfg, "irc.port", null));
        _enableAnnounce = PropertyHelper.getProperty(ircCfg, "irc.enable.announce", "false").equals("true");
        _singlesession = PropertyHelper.getProperty(ircCfg, "irc.singlesession", "false").equals("true");
        Debug.setDebugLevel(Integer.parseInt(PropertyHelper.getProperty(ircCfg, "irc.debuglevel", "15")));
        CaseInsensitiveHashMap<String, ChannelConfig> oldChannelMap = null;
        if (_channelMap != null) {
            oldChannelMap = _channelMap;
        } else {
            oldChannelMap = new CaseInsensitiveHashMap<String, ChannelConfig>();
        }
        synchronized (this) {
            _channelMap = new CaseInsensitiveHashMap<String, ChannelConfig>();
            for (int i = 1; ; i++) {
                String channelName = PropertyHelper.getProperty(ircCfg, "irc.channel." + i, null);
                if (channelName == null) break;
                String blowKey = PropertyHelper.getProperty(ircCfg, "irc.channel." + i + ".blowkey", null);
                String chanKey = PropertyHelper.getProperty(ircCfg, "irc.channel." + i + ".chankey", null);
                String permissions = PropertyHelper.getProperty(ircCfg, "irc.channel." + i + ".perms", null);
                if (i == 1) {
                    _primaryChannelName = channelName.toUpperCase();
                }
                _channelMap.put(channelName, new ChannelConfig(blowKey, chanKey, permissions));
            }
            String[] events = { "dele", "wipe", "slave", "invite", "mkdir", "request", "reqfilled", "rmdir", "pre", "shutdown", "log", "reqdel" };
            HashMap<String, ArrayList<String>> newEventChannelMap = new HashMap<String, ArrayList<String>>();
            for (String event : events) {
                ArrayList<String> eChans = new ArrayList<String>();
                for (int i = 1; ; i++) {
                    String channel = PropertyHelper.getProperty(ircCfg, "irc.event." + event + ".channel." + i, null);
                    if (channel == null) break;
                    eChans.add(channel.trim().toLowerCase());
                }
                if (!eChans.isEmpty()) newEventChannelMap.put(event, eChans);
            }
            _eventChannelMap = newEventChannelMap;
            _sections = new Hashtable<String, SectionSettings>();
            for (int i = 1; ; i++) {
                String name = PropertyHelper.getProperty(ircCfg, "irc.section." + i, null);
                if (name == null) {
                    break;
                }
                String chan = PropertyHelper.getProperty(ircCfg, "irc.section." + i + ".channel", null);
                if (chan == null) {
                    chan = _primaryChannelName;
                }
                _sections.put(name, new SectionSettings(ircCfg, i, chan));
            }
            if (_conn == null) {
                connect(ircCfg);
            }
            if ((!_conn.getClientState().getServer().equals(_server)) || (_conn.getClientState().getPort() != _port)) {
                logger.info("Reconnecting due to server change");
                connect(ircCfg);
            }
            if (_conn.getClientState().getNick() != null && !_conn.getClientState().getNick().getNick().equals(PropertyHelper.getProperty(ircCfg, "irc.nick", null))) {
                logger.info("Switching to new nick");
                _autoRegister.disable();
                _autoRegister = addAutoRegister(ircCfg);
                _conn.sendCommand(new NickCommand(PropertyHelper.getProperty(ircCfg, "irc.nick", null)));
            }
            for (Iterator iter = new ArrayList(Collections.list(getIRCConnection().getClientState().getChannelNames())).iterator(); iter.hasNext(); ) {
                String currentChannel = (String) iter.next();
                if (_channelMap.containsKey(currentChannel)) {
                    ChannelConfig newCC = _channelMap.get(currentChannel);
                    ChannelConfig oldCC = oldChannelMap.get(currentChannel);
                    if (newCC == null || oldCC == null) {
                        logger.debug("This is a bug! report me! -- channel=" + currentChannel + " newCC=" + newCC + " oldCC=" + oldCC, new Throwable());
                        continue;
                    }
                    newCC.setAutoJoin(oldCC.getAutoJoin());
                } else {
                    ChannelConfig oldCC = oldChannelMap.get(currentChannel);
                    if (oldCC == null) {
                        logger.debug("This is a bug! report me! -- channel=" + currentChannel + " oldCC=null", new Throwable());
                        continue;
                    }
                    oldCC.getAutoJoin().disable();
                    _conn.sendCommand(new PartCommand(currentChannel));
                    _conn.getClientState().removeChannel(currentChannel);
                    _conn.removeCommandObserver(oldCC.getAutoJoin());
                }
            }
        }
        for (String channelName : _channelMap.keySet()) {
            ChannelConfig cc = _channelMap.get(channelName);
            if (cc == null) {
                logger.debug("This is a bug! report me! -- channel=" + channelName + " cc=null", new Throwable());
                continue;
            }
            if (cc.getAutoJoin() == null) {
                cc.setAutoJoin(new AutoJoin(_conn, channelName, cc._chanKey));
            }
        }
        _raceleader = new LRUCache<String, User>(20);
        try {
            _maxUserAnnounce = Integer.parseInt(PropertyHelper.getProperty(ircCfg, "irc.max.racers", "100"));
        } catch (NumberFormatException e) {
            logger.warn("Invalid setting in irc.conf: irc.max.racers", e);
            _maxUserAnnounce = 100;
        }
        try {
            _maxGroupAnnounce = Integer.parseInt(PropertyHelper.getProperty(ircCfg, "irc.max.groups", "100"));
        } catch (NumberFormatException e) {
            logger.warn("Invalid setting in irc.conf: irc.max.groups", e);
            _maxGroupAnnounce = 100;
        }
    }
}
