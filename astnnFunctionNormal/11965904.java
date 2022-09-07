class BackupThread extends Thread {
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
            log.warn("Unexpected exception from downstream.", e.getCause());
            e.getChannel().close();
        }
}
