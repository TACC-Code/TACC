class BackupThread extends Thread {
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            this.time = System.currentTimeMillis() - this.time;
            ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
            DNSMessage msg = new DNSMessage(buffer);
            println(msg);
            e.getChannel().close();
        }
}
