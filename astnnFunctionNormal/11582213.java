class BackupThread extends Thread {
    public void init() {
        executor = Executors.newFixedThreadPool(2);
        clientBootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(executor, executor));
        clientBootstrap.setPipelineFactory(new RtspClientPipelineFactory(this));
        try {
            URI rtspURI = new URI(uri);
            String host = rtspURI.getHost();
            int port = rtspURI.getPort();
            ChannelFuture future = clientBootstrap.connect(new InetSocketAddress(host, port > 0 ? port : 554));
            future.addListener(new ChannelFutureListener() {

                public void operationComplete(ChannelFuture arg0) throws Exception {
                    channel = arg0.getChannel();
                    sendOptions();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
