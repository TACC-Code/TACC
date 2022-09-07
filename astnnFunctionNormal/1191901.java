class BackupThread extends Thread {
    private void onReceivePortPredRequest(IEnvelope env, MappedAddrRequest request) {
        DatagramChannel dc = openDatagramChannel();
        try {
            BlockingHook bh = BlockingHook.createAwaitMessageHook(dc, UDPPing.class);
            IEnvelope ping = null;
            connBroker.getCommFacade().getMessageProcessor().installHook(bh, bh.getPredicate());
            try {
                try {
                    InetSocketAddress addr = new InetSocketAddress(InetAddressUtils.getMostProbableExternalAddress(), dc.socket().getLocalPort());
                    connBroker.getCommFacade().sendTCPMessage((SocketChannel) env.getChannel(), new MappedAddrAnswer(myPeerID, env.getSender().getPeerID(), addr));
                } catch (IOException e) {
                    Logger.logError(e, "Error handling port prediction request.");
                    return;
                }
                ping = bh.waitForMessage();
            } finally {
                connBroker.getCommFacade().getMessageProcessor().removeHook(bh);
            }
            if (ping == null) Logger.logWarning("PortPrediction did not receive the required Ping-message!");
            MappedAddressResult result = new MappedAddressResult(myPeerID, env.getSender().getPeerID(), ping != null ? ping.getSender().getAddress() : null);
            try {
                connBroker.getCommFacade().sendTCPMessage((SocketChannel) env.getChannel(), result);
            } catch (IOException e) {
                Logger.logError(e, "Error sending results of port prediction!");
            }
        } finally {
            try {
                dc.close();
            } catch (IOException e) {
            }
        }
    }
}
