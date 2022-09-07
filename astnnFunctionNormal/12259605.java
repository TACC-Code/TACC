class BackupThread extends Thread {
        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            closeRsocketChannel(userToken, ctx.getChannel());
        }
}
