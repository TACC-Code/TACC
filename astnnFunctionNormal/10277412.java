class BackupThread extends Thread {
    private DatagramChannel getChannel(int port) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        DatagramSocket s = channel.socket();
        s.setReceiveBufferSize(64 * 1024);
        s.setSendBufferSize(64 * 1024);
        s.setReuseAddress(true);
        s.bind(new InetSocketAddress(port));
        return channel;
    }
}
