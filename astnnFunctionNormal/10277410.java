class BackupThread extends Thread {
    public void start(int port) throws IOException {
        channel = getChannel(port);
        NIODispatcher.instance().registerReadWrite(channel, this);
    }
}
