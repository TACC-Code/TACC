class BackupThread extends Thread {
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        if (null != conn) {
            conn.closeRConnection(ctx.getChannel());
        }
    }
}
