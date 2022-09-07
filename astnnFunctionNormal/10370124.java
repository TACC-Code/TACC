class BackupThread extends Thread {
    protected synchronized ChannelFuture getRemoteFuture() {
        if (null == remoteFuture || !remoteFuture.getChannel().isConnected()) {
            ChannelPipeline pipeline = Channels.pipeline();
            pipeline.addLast("decoder", new HttpResponseDecoder());
            pipeline.addLast("encoder", new HttpRequestEncoder());
            pipeline.addLast("handler", new HttpResponseHandler());
            String connectHost = auth.domain;
            int connectPort = auth.port;
            C4ClientConfiguration cfg = C4ClientConfiguration.getInstance();
            if (null != cfg.getLocalProxy()) {
                ProxyInfo info = cfg.getLocalProxy();
                connectHost = info.host;
                connectPort = info.port;
            }
            connectHost = HostsHelper.getMappingHost(connectHost);
            if (logger.isDebugEnabled()) {
                logger.debug("Connect remote proxy server " + connectHost + ":" + connectPort);
            }
            remoteFuture = getClientSocketChannelFactory().newChannel(pipeline).connect(new InetSocketAddress(connectHost, connectPort));
        }
        return remoteFuture;
    }
}
