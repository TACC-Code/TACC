class BackupThread extends Thread {
    private void createReaderServerConnections() {
        for (SimpleNetworkReaderOperator reader : _inputOperators) {
            try {
                ServerSocket socket = OhuaServerSocketFactory.getInstance().createServerSocket(reader.getLocalPort());
                _openServerSockets.add(socket);
                System.out.println("Listening on " + socket.getInetAddress());
                socket.getChannel().register(getGlobalSelector(), SelectionKey.OP_ACCEPT, reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
