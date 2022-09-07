class BackupThread extends Thread {
    public TcpChannel getChannel() throws InterruptedException {
        return channelQueue.take();
    }
}
