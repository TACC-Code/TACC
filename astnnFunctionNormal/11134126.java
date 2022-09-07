class BackupThread extends Thread {
    public SocketChannel getChannel() throws IOException {
        if (channel == null) {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
        }
        return channel;
    }
}
