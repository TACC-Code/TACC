class BackupThread extends Thread {
    public void handleAcceptable(SelectionKey selKey) throws IOException {
        ServerSocketChannel socketChannel = (ServerSocketChannel) selKey.channel();
        ServerSocket serverSocket = socketChannel.socket();
        _metaDataSocket = serverSocket.accept();
        _metaDataSocket.getChannel().configureBlocking(false);
        _metaDataSocket.getChannel().register(_selector, SelectionKey.OP_READ);
        selKey.cancel();
    }
}
