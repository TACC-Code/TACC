class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: " + FactorialClient.class.getSimpleName() + " <host> <port> <count>");
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        int count = Integer.parseInt(args[2]);
        if (count <= 0) {
            throw new IllegalArgumentException("count must be a positive integer.");
        }
        ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new FactorialClientPipelineFactory(count));
        ChannelFuture connectFuture = bootstrap.connect(new InetSocketAddress(host, port));
        Channel channel = connectFuture.awaitUninterruptibly().getChannel();
        FactorialClientHandler handler = (FactorialClientHandler) channel.getPipeline().getLast();
        System.err.format("Factorial of %,d is: %,d", count, handler.getFactorial());
        bootstrap.releaseExternalResources();
    }
}
