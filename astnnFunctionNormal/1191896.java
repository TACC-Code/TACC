class BackupThread extends Thread {
    private void onReceiveUDPPunch(IEnvelope env, UDPPunchRequest message) {
        final DatagramChannel dc = natConnector.getRegisteredUDPChannel(message.getFromPort());
        if (dc == null) {
            UDPPunchAnswer answer = new UDPPunchAnswer(myPeerID, env.getSender().getPeerID(), message, false, "Channel with that port is not registered!");
            try {
                commFacade.sendTCPMessage((SocketChannel) env.getChannel(), answer);
            } catch (IOException e) {
            }
        } else {
            try {
                InetSocketAddress pred[] = null;
                if (message.needPortPrediction()) {
                    pred = connBroker.getMappedAddrResolver().getPossiblePorts(dc, message.getPunchTargets()[0]);
                }
                for (InetSocketAddress addr : message.getPunchTargets()) commFacade.sendUDPMessage(dc, new UDPPing(myPeerID), addr);
                commFacade.sendTCPMessage((SocketChannel) env.getChannel(), new UDPPunchAnswer(myPeerID, env.getSender().getPeerID(), message, true, "", pred));
            } catch (IOException e) {
                Logger.logError(e, "Exception durion port prediction!");
            }
        }
    }
}
