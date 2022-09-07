class BackupThread extends Thread {
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        log.debug("Channel disconnected from " + e.getChannel().getRemoteAddress() + " is channel " + e.getChannel().getId());
        this.onChannelDisconnected(e.getChannel());
    }
}
