class BackupThread extends Thread {
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
}
