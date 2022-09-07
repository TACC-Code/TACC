class BackupThread extends Thread {
    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        AgentSession session = new AgentSession(e.getChannel().getRemoteAddress().toString());
        ctx.setAttachment(session);
        super.channelOpen(ctx, e);
    }
}
