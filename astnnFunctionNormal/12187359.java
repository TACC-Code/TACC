class BackupThread extends Thread {
        @Override
        public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) {
            System.err.println(e.getCause());
            e.getChannel().close();
        }
}
