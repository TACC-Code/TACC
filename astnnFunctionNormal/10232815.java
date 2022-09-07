class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        LocalAddress socketAddress = new LocalAddress("1");
        ServerBootstrap sb = new ServerBootstrap(new DefaultLocalServerChannelFactory());
        EchoServerHandler handler = new EchoServerHandler();
        sb.getPipeline().addLast("handler", handler);
        sb.bind(socketAddress);
        ClientBootstrap cb = new ClientBootstrap(new DefaultLocalClientChannelFactory());
        cb.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new StringDecoder(), new StringEncoder(), new LoggingHandler(InternalLogLevel.INFO));
            }
        });
        ChannelFuture channelFuture = cb.connect(socketAddress);
        channelFuture.awaitUninterruptibly();
        System.out.println("Enter text (quit to end)");
        ChannelFuture lastWriteFuture = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (; ; ) {
            String line = in.readLine();
            if (line == null || "quit".equalsIgnoreCase(line)) {
                break;
            }
            lastWriteFuture = channelFuture.getChannel().write(line);
        }
        if (lastWriteFuture != null) {
            lastWriteFuture.awaitUninterruptibly();
        }
        channelFuture.getChannel().close();
        channelFuture.getChannel().getCloseFuture().awaitUninterruptibly();
        cb.releaseExternalResources();
        sb.releaseExternalResources();
    }
}
