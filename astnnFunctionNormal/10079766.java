class BackupThread extends Thread {
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        initExecClient();
        SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);
        ChannelFuture handshakeFuture = sslHandler.handshake();
        handshakeFuture.addListener(new ChannelFutureListener() {

            public void operationComplete(ChannelFuture future) throws Exception {
                logger.debug("Handshake: " + future.isSuccess(), future.getCause());
                if (future.isSuccess()) {
                    factory.addChannel(future.getChannel());
                } else {
                    future.getChannel().close();
                }
            }
        });
    }
}
