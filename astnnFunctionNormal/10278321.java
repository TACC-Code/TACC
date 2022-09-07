class BackupThread extends Thread {
    void connect(final Session session) throws IOException {
        SocketChannel sChannel = SocketChannel.open();
        Connection con = new Connection(this, sChannel, session) {

            jerklib.Channel getChannel(java.lang.String A) {
                return new Channel(A, session);
            }
        };
        session.setConnection(con);
        socChanMap.put(sChannel, session);
    }
}
