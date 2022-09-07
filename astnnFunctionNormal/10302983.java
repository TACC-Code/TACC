class BackupThread extends Thread {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object msg = e.getMessage();
        if (msg instanceof ChannelBuffer) {
            ChannelBuffer buf = (ChannelBuffer) msg;
            byte[] raw = new byte[buf.readableBytes()];
            buf.readBytes(raw);
            Buffer content = Buffer.wrapReadableContent(raw);
            while (content.readable()) {
                Event ev = EventDispatcher.getSingletonInstance().parse(content);
                if (ev instanceof RSocketAcceptedEvent) {
                    RSocketAcceptedEvent rev = (RSocketAcceptedEvent) ev;
                    if (logger.isDebugEnabled()) {
                        logger.debug("Recv connection from " + rev.domain + ":" + rev.port);
                    }
                    C4ServerAuth auth = new C4ServerAuth();
                    auth.domain = rev.domain;
                    auth.port = rev.port;
                    conn = RSocketProxyConnection.saveRConnection(auth, ctx.getChannel());
                } else {
                    conn.handleEvent(ev);
                }
            }
        } else {
            logger.error("Unsupported message type:" + msg.getClass().getName());
        }
    }
}
