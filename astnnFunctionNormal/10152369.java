class BackupThread extends Thread {
    public SocketChannel getChannel() {
        return this.getClient().getConnection().getChannel();
    }
}
