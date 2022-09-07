class BackupThread extends Thread {
    protected SocketChannel getSocketChannel() throws IOException {
        if (isUseKeepAlive() && savedSock != null) {
            return savedSock;
        }
        int port;
        try {
            port = Integer.parseInt(getPort());
        } catch (NumberFormatException ex) {
            log.warn("Wrong port number: " + getPort() + ", defaulting to 80", ex);
            port = 80;
        }
        InetSocketAddress address = new InetSocketAddress(getHostName(), port);
        savedSock = (SocketChannel) getChannel();
        savedSock.connect(address);
        return savedSock;
    }
}
