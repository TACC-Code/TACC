class BackupThread extends Thread {
    public ServerSocketChannel getChannel() {
        return (ServerSocketChannel) super.getChannel();
    }
}
