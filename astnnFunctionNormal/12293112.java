class BackupThread extends Thread {
    @Override
    protected IConnectionInfo<DatagramChannel> establishConnImpl(IPeer remotePeer, ConnectionRequestType type) {
        final boolean DETAILED_LOGGING = true;
        ICommFacade commFacade = getConnBroker().getCommFacade();
        IMessageProcessor msgProc = commFacade.getMessageProcessor();
        SocketChannel controlChannel = getConnBroker().getControlChannelManager().getControlChannelFor(remotePeer.getPeerID());
        IPeerID myself = getConnBroker().getCallback().getOwnPeerID();
        if (DETAILED_LOGGING) Logger.log("[PortRestrictedMech] Started PortRestricted Traversal Mechanism");
        if (controlChannel == null) {
            Logger.logWarning("[PortRestrictedMech] No control channel available, PortRestricted failed!");
            return null;
        }
        DatagramChannel dc = openDatagramChannel();
        commFacade.getChannelManager().registerChannel(dc);
        InetSocketAddress nextAddresses[] = getConnBroker().getMappedAddrResolver().getPossiblePorts(dc, remotePeer.getAddress());
        if (nextAddresses == null || nextAddresses.length == 0) {
            Logger.logWarning("[PortRestrictedMech] Port prediction did not work. UDPHolePunching fails!");
            return null;
        }
        BlockingHook waitForPunchAnswer = BlockingHook.createAwaitMessageHook(controlChannel, PerformUDPPunchAnswer.class);
        msgProc.installHook(waitForPunchAnswer, waitForPunchAnswer.getPredicate());
        PerformUDPPunchAnswer answer = null;
        if (DETAILED_LOGGING) Logger.log("[PortRestrictedMech] Preparing to send DoublePunch");
        try {
            PerformUDPPunchRequest punch = new PerformUDPPunchRequest(myself, remotePeer.getPeerID(), remotePeer.getPeerID(), remotePeer.getAddress().getPort(), nextAddresses);
            try {
                commFacade.sendTCPMessage(controlChannel, punch);
                IEnvelope env = waitForPunchAnswer.waitForMessage(1000);
                if (env != null) answer = (PerformUDPPunchAnswer) env.getMessage();
            } catch (IOException e) {
                Logger.logError(e, "[PortRestrictedMech] Unable to send DoublePunch-message to controlhost.");
                return null;
            }
        } finally {
            msgProc.removeHook(waitForPunchAnswer);
        }
        if (answer == null) {
            Logger.logWarning("[PortRestrictedMech] Did not receive answer of PerformUDPPunchRequest!");
            return null;
        }
        BlockingHook waitForPong = BlockingHook.createAwaitMessageHook(dc, UDPPong.class);
        msgProc.installHook(waitForPong, waitForPong.getPredicate());
        try {
            for (InetSocketAddress punchBack : answer.getPunchBackAddresses()) {
                try {
                    commFacade.sendUDPMessage(dc, new UDPPing(myself), punchBack);
                } catch (IOException e) {
                }
            }
            IEnvelope env = waitForPong.waitForMessage();
            if (env != null) {
                try {
                    Logger.log("[PortRestrictedMech] Got the UDP pong from " + env.getSender().getAddress());
                    dc.connect(env.getSender().getAddress());
                } catch (IOException e) {
                }
                System.out.println("[UDPHolePunching] Successfully established connection to " + remotePeer);
                return new ConnectionInfo<DatagramChannel>(dc, false, true, null, "UDP Holepunching");
            } else Logger.logWarning("[PortRestrictedMech] Got no UDP pong! Returning null...");
        } finally {
            msgProc.removeHook(waitForPong);
        }
        return null;
    }
}
