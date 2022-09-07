class BackupThread extends Thread {
    private void connect(Object[] args) throws ProtocolException {
        @SuppressWarnings("unchecked") Map<String, String> sharedPreferences = (Map<String, String>) serviceResponse.respond(IAccountServiceResponse.RES_GETFROMSTORAGE, getServiceId(), IAccountServiceResponse.SHARED_PREFERENCES, options.keySet());
        if (sharedPreferences == null) {
            throw new ProtocolException("Error getting preferences");
        }
        try {
            String jid = sharedPreferences.get(JID);
            String[] jidParams = jid.split("@");
            un = jidParams[0];
            serviceName = jidParams[1];
        } catch (Exception e2) {
            log(e2);
        }
        pw = sharedPreferences.get(PASSWORD);
        try {
            loginPort = Integer.parseInt(sharedPreferences.get(LOGIN_PORT));
        } catch (Exception e2) {
        }
        String host = sharedPreferences.get(LOGIN_HOST);
        if (host != null) {
            loginHost = host;
        }
        if (loginHost == null || loginPort < 1 || un == null || pw == null) {
            throw new ProtocolException("Error: no auth data");
        }
        if (serviceName == null) {
            serviceName = loginHost;
        }
        String ping = sharedPreferences.get(AccountService.PING_TIMEOUT);
        if (ping != null) {
            try {
                pingTimeout = Integer.parseInt(ping);
            } catch (Exception e) {
            }
        }
        if (args.length > 9) {
            isSecure = true;
        } else {
            isSecure = false;
        }
        new Thread("XMPP connector " + getJID()) {

            @Override
            public void run() {
                SmackConfiguration.setPacketReplyTimeout(120000);
                ConnectionConfiguration config = new ConnectionConfiguration(loginHost, loginPort);
                String login;
                if (isGmail()) {
                    config.setSASLAuthenticationEnabled(false);
                    login = un;
                } else {
                    login = un;
                }
                config.setServiceName(serviceName);
                configure(ProviderManager.getInstance());
                connection = new XMPPConnection(config);
                try {
                    isContactListReady = false;
                    serviceResponse.respond(IAccountServiceResponse.RES_CONNECTING, serviceId, 1);
                    connection.connect();
                    serviceResponse.respond(IAccountServiceResponse.RES_CONNECTING, serviceId, 2);
                    new ServiceDiscoveryManager(connection);
                    connection.login(login, pw, "AsiaIM");
                    Roster roster = connection.getRoster();
                    roster.addRosterListener(XMPPService.this);
                    connection.addConnectionListener(XMPPService.this);
                    serviceResponse.respond(IAccountServiceResponse.RES_CONNECTING, serviceId, 3);
                    connection.sendPacket(XMPPEntityAdapter.userStatus2XMPPPresence(onlineInfo.userStatus));
                    serviceResponse.respond(IAccountServiceResponse.RES_CONNECTING, serviceId, 5);
                    while (!roster.rosterInitialized) {
                        Thread.sleep(500);
                    }
                    List<Buddy> buddies = XMPPEntityAdapter.rosterEntryCollection2BuddyList(XMPPService.this, roster.getEntries());
                    List<BuddyGroup> groups = XMPPEntityAdapter.rosterGroupCollection2BuddyGroupList(roster.getGroups(), getJID(), serviceId);
                    serviceResponse.respond(IAccountServiceResponse.RES_CONNECTING, serviceId, 7);
                    connection.getChatManager().addChatListener(XMPPService.this);
                    try {
                        chechGroupChatAvailability();
                        buddies.addAll(getJoinedChatRooms());
                    } catch (Exception e) {
                        log(e);
                    }
                    serviceResponse.respond(IAccountServiceResponse.RES_CLUPDATED, getServiceId(), buddies, groups);
                    serviceResponse.respond(IAccountServiceResponse.RES_CONNECTING, serviceId, 9);
                    serviceResponse.respond(IAccountServiceResponse.RES_ACCOUNTUPDATED, serviceId, onlineInfo);
                    serviceResponse.respond(IAccountServiceResponse.RES_CONNECTED, getServiceId());
                    getPersonalInfo(getJID());
                    isContactListReady = true;
                    checkCachedInfos();
                    messageEventManager = new MessageEventManager(connection);
                    if (onlineInfo.userStatus != Buddy.ST_INVISIBLE) {
                        messageEventManager.addMessageEventRequestListener(messageEventListener);
                    }
                    messageEventManager.addMessageEventNotificationListener(XMPPService.this);
                    ftm = new FileTransferManager(connection);
                    ftm.addFileTransferListener(XMPPService.this);
                    sendKeepalive();
                } catch (Exception e) {
                    log(e);
                    connection = null;
                    connectionClosedOnError(e);
                }
            }
        }.start();
    }
}
