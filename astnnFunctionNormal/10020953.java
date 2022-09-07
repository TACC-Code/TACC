class BackupThread extends Thread {
    public SocketOrChannelConnectionImpl(ORB orb, Acceptor acceptor, Socket socket, boolean useSelectThreadToWait, boolean useWorkerThread) {
        this(orb, useSelectThreadToWait, useWorkerThread);
        this.socket = socket;
        socketChannel = socket.getChannel();
        if (socketChannel != null) {
            try {
                boolean isBlocking = !useSelectThreadToWait;
                socketChannel.configureBlocking(isBlocking);
            } catch (IOException e) {
                RuntimeException rte = new RuntimeException();
                rte.initCause(e);
                throw rte;
            }
        }
        this.acceptor = acceptor;
        serverRequestMap = Collections.synchronizedMap(new HashMap());
        isServer = true;
        state = ESTABLISHED;
    }
}
