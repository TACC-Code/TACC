class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        LocalAddress socketAddress = new LocalAddress("1");
        OrderedMemoryAwareThreadPoolExecutor eventExecutor = new OrderedMemoryAwareThreadPoolExecutor(5, 1000000, 10000000, 100, TimeUnit.MILLISECONDS);
        ServerBootstrap sb = new ServerBootstrap(new DefaultLocalServerChannelFactory());
        sb.setPipelineFactory(new LocalServerPipelineFactory(eventExecutor));
        sb.bind(socketAddress);
        ClientBootstrap cb = new ClientBootstrap(new DefaultLocalClientChannelFactory());
        cb.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new StringDecoder(), new StringEncoder(), new LoggingHandler(InternalLogLevel.INFO));
            }
        });
        String[] commands = { "First", "Second", "Third", "quit" };
        for (int j = 0; j < 5; j++) {
            System.err.println("Start " + j);
            ChannelFuture channelFuture = cb.connect(socketAddress);
            channelFuture.awaitUninterruptibly();
            if (!channelFuture.isSuccess()) {
                System.err.println("CANNOT CONNECT");
                channelFuture.getCause().printStackTrace();
                break;
            }
            ChannelFuture lastWriteFuture = null;
            for (String line : commands) {
                lastWriteFuture = channelFuture.getChannel().write(line);
            }
            if (lastWriteFuture != null) {
                lastWriteFuture.awaitUninterruptibly();
            }
            channelFuture.getChannel().close();
            channelFuture.getChannel().getCloseFuture().awaitUninterruptibly();
            System.err.println("End " + j);
        }
        cb.releaseExternalResources();
        sb.releaseExternalResources();
        eventExecutor.shutdownNow();
    }
}
