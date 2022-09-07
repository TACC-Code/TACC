class BackupThread extends Thread {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        logger.error("Unhandled exception in handler", e.getCause());
        e.getChannel().close();
        throw new Exception(e.getCause());
    }
}
