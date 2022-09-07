class BackupThread extends Thread {
    public void connect(long timeout, TimeUnit unit) throws Exception {
        bootstrap.setOption("connectTimeoutMillis", TimeUnit.MILLISECONDS.convert(timeout, unit));
        ChannelFuture cf = bootstrap.connect(new InetSocketAddress(host, port));
        cf.await();
        if (!cf.isSuccess()) {
            throw (Exception) cf.getCause();
        }
        channel = cf.getChannel();
    }
}
