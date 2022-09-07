class BackupThread extends Thread {
    public void sendTest() throws Exception {
        DatagramChannelFactory factory = new NioDatagramChannelFactory(Executors.newCachedThreadPool());
        ClientBootstrap bootstrap = new ClientBootstrap(factory);
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() {
                return Channels.pipeline(new DNSClientHandler());
            }
        });
        bootstrap.setOption("broadcast", "false");
        bootstrap.setOption("sendBufferSize", 512);
        bootstrap.setOption("receiveBufferSize", 512);
        InetSocketAddress address = new InetSocketAddress(findDNSServer(), 53);
        ChannelFuture future = bootstrap.connect(address);
        future.awaitUninterruptibly();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
        }
        future.getChannel().getCloseFuture().awaitUninterruptibly();
        factory.releaseExternalResources();
    }
}
