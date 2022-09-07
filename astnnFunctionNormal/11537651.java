class BackupThread extends Thread {
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        log.debug("Channel connected from " + e.getChannel().getRemoteAddress() + " is channel " + e.getChannel().getId());
        SessionContext session = new SessionContext(e.getChannel(), new BEncodeChannelWriter(e.getChannel()));
        this.onChannelConnected(e.getChannel(), session);
    }
}
