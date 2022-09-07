class BackupThread extends Thread {
    public void handle(INonBlockingConnection conn, EraseMessage message) throws IOException {
        Main.broadcast(message, PeterHi.SOCKET, false);
        SocketServer ss = SocketServer.getInstance();
        ClientHandle cs = ss.get(conn);
        RemoveAllShapes rs = new RemoveAllShapes(cs.getChannel());
        Persister.getInstance().execute(rs);
    }
}
