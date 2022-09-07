class BackupThread extends Thread {
    @Override
    public void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        GsConnection con = new GsConnection(socketChannel);
        Dispatcher readDispatcher = IOServer.getInstance().getReadDispatcher();
        SelectionKey readKey = readDispatcher.register(socketChannel, SelectionKey.OP_READ, con);
        Dispatcher writeDispatcher = IOServer.getInstance().getWriteDispatcher();
        if (writeDispatcher != readDispatcher) con.setWriteKey(writeDispatcher.register(socketChannel, 0, con)); else con.setWriteKey(readKey);
    }
}
