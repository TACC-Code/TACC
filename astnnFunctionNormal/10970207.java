class BackupThread extends Thread {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.error("exception: {}", e.getCause().getMessage());
        writer.close();
        e.getChannel().close();
    }
}
