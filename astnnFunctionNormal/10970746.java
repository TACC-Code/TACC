class BackupThread extends Thread {
    public void handleAcceptable(SelectionKey selKey) throws IOException {
        ServerSocketChannel socketChannel = (ServerSocketChannel) selKey.channel();
        ServerSocket serverSocket = socketChannel.socket();
        Socket socket = serverSocket.accept();
        socket.setOOBInline(true);
        _openSockets.add(socket);
        SimpleNetworkReaderOperator readerOp = (SimpleNetworkReaderOperator) selKey.attachment();
        readerOp.setChannel(socket.getChannel());
        socket.getChannel().configureBlocking(false);
        socket.getChannel().register(getGlobalSelector(), SelectionKey.OP_READ, readerOp);
    }
}
