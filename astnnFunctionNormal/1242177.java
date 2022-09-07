class BackupThread extends Thread {
    private void connect() {
        ChannelFuture future = bootstrap.connect(address);
        channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            System.err.println("Client Not Connected");
            future.getCause().printStackTrace();
            return;
        }
    }
}
