class BackupThread extends Thread {
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelConnected(ctx, e);
        state = State.CONNECTED;
        channel = ctx.getChannel();
        inetAddress = ((InetSocketAddress) e.getChannel().getRemoteAddress()).getAddress();
        log.info("Channel connected Ip:" + inetAddress.getHostAddress());
    }
}
