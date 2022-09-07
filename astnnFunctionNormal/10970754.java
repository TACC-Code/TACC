class BackupThread extends Thread {
    private void createWriterConnections() throws IOException {
        for (SimpleNetworkWriterOperator writer : _runningOutputOperators) {
            Socket socket = OhuaSocketFactory.getInstance().createSocket(writer.getRemoteIP(), writer.getRemotePort());
            SocketChannel channel = socket.getChannel();
            channel.configureBlocking(false);
            writer.setChannel(channel);
            channel.finishConnect();
            channel.register(getGlobalSelector(), SelectionKey.OP_WRITE, writer);
            handleOOBsupport(socket, writer);
        }
    }
}
