class BackupThread extends Thread {
    public ServerSocketChannel getChannel() {
        return delegate.getChannel();
    }
}
