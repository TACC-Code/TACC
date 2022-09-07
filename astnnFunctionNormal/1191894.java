class BackupThread extends Thread {
    private void onReceivePerformUDPPunch(IEnvelope env, PerformUDPPunchRequest request) {
        IPeerID senderID = env.getSender().getPeerID();
        UDPPunchAnswer punchAnswer = writePunch(request.getTargetPeer(), request.getFromPort(), request.getPunchTargets(), true);
        PerformUDPPunchAnswer answer = null;
        if (punchAnswer == null) {
            answer = new PerformUDPPunchAnswer(myPeerID, senderID, request, false, "Did not receive punch answer!", new InetSocketAddress[0]);
        } else if (!punchAnswer.isSuccessful()) {
            answer = new PerformUDPPunchAnswer(myPeerID, senderID, request, false, "Punching failed: " + punchAnswer.getReason(), new InetSocketAddress[0]);
        } else answer = new PerformUDPPunchAnswer(myPeerID, senderID, request, true, "", punchAnswer.getPredicatedAddr());
        try {
            connBroker.getCommFacade().sendTCPMessage((SocketChannel) env.getChannel(), answer);
        } catch (IOException e) {
            Logger.logError(e, "Failed to write PerformUDPPunchAnswer back to " + env.getSender());
        }
    }
}
