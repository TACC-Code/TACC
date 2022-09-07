class BackupThread extends Thread {
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            log.info("SERVER - messageReceived() " + e);
            e.getChannel().write(e.getMessage());
        }
}
