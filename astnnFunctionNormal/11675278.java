class BackupThread extends Thread {
    public void receivedMessage(ByteBuffer message) {
        PacketType packetType = NetworkProtocol.getPacketHeader(message);
        switch(packetType) {
            case JOIN_CHANNEL:
                {
                    ChannelManager channelManager = AppContext.getChannelManager();
                    String channelName = NetworkProtocol.chatChannelPrefix + JoinChannelPacket.decode(message);
                    try {
                        channelManager.getChannel(channelName).join(getSession());
                        System.out.println("Moved user into existing channel " + channelName);
                    } catch (NameNotBoundException e) {
                        channelManager.createChannel(channelName, new ChatChannelListener(), Delivery.UNRELIABLE).join(getSession());
                        System.out.println("Created new channel " + channelName);
                    }
                    break;
                }
            case LEAVE_CHANNEL:
                {
                    ChannelManager channelManager = AppContext.getChannelManager();
                    String channelName = NetworkProtocol.chatChannelPrefix + LeaveChannelPacket.decode(message);
                    try {
                        Channel channel = channelManager.getChannel(channelName);
                        channel.leave(getSession());
                        AppContext.getTaskManager().scheduleTask(new CleanChannelTask(channelName), 500);
                    } catch (NameNotBoundException e) {
                    }
                    break;
                }
            case WHISPER_CHAT:
                {
                    String[] contents = ChatWhisperPacket.decode(message);
                    String targetName = contents[0];
                    String messageContents = contents[1];
                    ClientSession targetSession = getSessionByName(targetName);
                    if (targetSession == null) getSession().send(SimpleChatPacket.create("Player " + targetName + " not found.")); else {
                        getSession().send(WhisperSuccessPacket.create(targetName, messageContents));
                        targetSession.send(ChatWhisperPacket.create(sessionName, messageContents));
                    }
                    break;
                }
            case PLAYER_INPUT:
                {
                    playerReference.get().getGameInputRef().getForUpdate().setByPacket(message);
                    break;
                }
            case CLIENT_STATE:
                {
                    ClientGameState currState = playerReference.get().getGameState();
                    if (currState == ClientGameState.FACTION_SELECT) {
                        ClientGameState reportedState = ClientStateTransitionPacket.decode(message);
                        if (reportedState != ClientGameState.FACTION_SELECT) throw new IllegalStateException("State Reported By Client does not Match Server Expectation");
                        byte factionSelect = ClientStateTransitionPacket.decodePBByteSelection(message);
                        ArrayList<ManagedReference<? extends PlayableFaction>> factionList = GameUniverse.getReadOnly().getPlayableFactions();
                        Player player = playerReference.getForUpdate();
                        player.setFaction(factionList.get(factionSelect));
                        player.transitionToShipSelection();
                    } else if (currState == ClientGameState.SHIP_SELECT || currState == ClientGameState.FRAGGED) {
                        ClientGameState reportedState = ClientStateTransitionPacket.decode(message);
                        if (reportedState != currState) throw new IllegalStateException("State Reported By Client does not Match Server Expectation");
                        byte shipSelect = ClientStateTransitionPacket.decodePBByteSelection(message);
                        Player player = playerReference.getForUpdate();
                        PlayerShipRecordHolder record = player.getShipRecords();
                        AppContext.getDataManager().markForUpdate(record);
                        ServerPlayerShip ship = player.getFaction().makeShipBySelection(shipSelect, playerReference);
                        record.associateShipWithRecord(ship);
                        player.transitionToInGame();
                    }
                    break;
                }
            case SERIAL_UNIVERSE:
                {
                    ArrayList<ByteBuffer> toSend = GameUniverse.getReadOnly().makeUniverseInformationPackets();
                    ClientSession session = getSession();
                    for (ByteBuffer b : toSend) session.send(b);
                    break;
                }
            case SERVER_DEBUG:
                {
                    break;
                }
            default:
                break;
        }
    }
}
