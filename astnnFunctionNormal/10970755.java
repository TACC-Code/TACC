class BackupThread extends Thread {
    private void handleOOBsupport(Socket socket, SimpleNetworkWriterOperator writer) throws IOException {
        socket.setOOBInline(true);
        Socket metadataSocket = OhuaSocketFactory.getInstance().createSocket(writer.getRemoteIP(), writer.getRemotePort());
        SocketChannel channel = metadataSocket.getChannel();
        channel.configureBlocking(false);
        writer.setMetadataChannel(channel);
        channel.finishConnect();
        channel.register(getGlobalSelector(), SelectionKey.OP_WRITE, writer);
        System.out.println("OOB connection successfully established!");
    }
}
