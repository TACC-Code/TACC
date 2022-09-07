class BackupThread extends Thread {
    public SocketOrChannelConnectionImpl(ORB orb, Acceptor acceptor, Socket socket) {
        this(orb, acceptor, socket, (socket.getChannel() == null ? false : orb.getORBData().connectionSocketUseSelectThreadToWait()), (socket.getChannel() == null ? false : orb.getORBData().connectionSocketUseWorkerThreadForEvent()));
    }
}
