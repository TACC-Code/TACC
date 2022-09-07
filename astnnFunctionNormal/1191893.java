class BackupThread extends Thread {
    private void onReceiveTCPConnRequest(IEnvelope env, TCPConnRequest request) {
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open(request.getTargetAddress());
            sc.configureBlocking(false);
            commFacade.getChannelManager().registerChannel(sc);
            if (sc == null || !sc.isOpen() || !sc.isConnected()) throw new IOException("Could not connect to " + request.getTargetAddress());
            TCPConnAnswer answer = new TCPConnAnswer(myPeerID, env.getSender().getPeerID(), request);
            commFacade.sendTCPMessage(sc, answer);
        } catch (IOException e) {
            Logger.logError(e, "Introduction with target of TCPConnRequest failed!");
            try {
                if (sc != null && sc.isOpen()) sc.close();
            } catch (IOException e1) {
            }
        }
    }
}
