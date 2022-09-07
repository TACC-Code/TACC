class BackupThread extends Thread {
    public SelectableChannel getChannel() {
        return socketChannel;
    }
}
