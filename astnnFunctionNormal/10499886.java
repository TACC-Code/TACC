class BackupThread extends Thread {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        logger.error("exceptionCaught.", e.getCause());
        if (null != eventService) {
            eventService.close();
            eventService = null;
        }
        if (e.getChannel().isOpen()) {
            e.getChannel().close();
        }
    }
}
