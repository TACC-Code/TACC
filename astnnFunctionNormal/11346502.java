class BackupThread extends Thread {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        if (e.getCause() instanceof ClosedChannelException) {
            System.err.println("Close before ending");
            return;
        }
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
