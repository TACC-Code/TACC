class BackupThread extends Thread {
    public SocketOrChannelConnectionImpl(ORB orb, CorbaContactInfo contactInfo, boolean useSelectThreadToWait, boolean useWorkerThread, String socketType, String hostname, int port) {
        this(orb, useSelectThreadToWait, useWorkerThread);
        this.contactInfo = contactInfo;
        try {
            socket = orb.getORBData().getSocketFactory().createSocket(socketType, new InetSocketAddress(hostname, port));
            socketChannel = socket.getChannel();
            if (socketChannel != null) {
                boolean isBlocking = !useSelectThreadToWait;
                socketChannel.configureBlocking(isBlocking);
            } else {
                setUseSelectThreadToWait(false);
            }
            if (orb.transportDebugFlag) {
                dprint(".initialize: connection created: " + socket);
            }
        } catch (Throwable t) {
            throw wrapper.connectFailure(t, socketType, hostname, Integer.toString(port));
        }
        state = OPENING;
    }
}
