class BackupThread extends Thread {
    public void handleDownstream(ChannelHandlerContext context, ChannelEvent evt) throws Exception {
        if (!(evt instanceof MessageEvent)) {
            context.sendDownstream(evt);
            return;
        }
        MessageEvent e = (MessageEvent) evt;
        Object o = e.getMessage();
        if (o instanceof RawFixMessage) {
            RawFixMessage rawFixMessage = (RawFixMessage) o;
            byte[] bytes = rawFixMessage.getBytes();
            write(context, e.getChannel(), e.getFuture(), copiedBuffer(bytes));
        } else {
            context.sendDownstream(evt);
        }
    }
}
