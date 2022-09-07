class BackupThread extends Thread {
        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            DNSMessage msg = new DNSMessage();
            msg.header().opcode(OpCode.QUERY);
            msg.header().rd(true);
            ResourceRecord rr = RRType.MX.newRecord();
            rr.name(new Name("iana.org."));
            msg.question().add(rr);
            ChannelBuffer buffer = ChannelBuffers.buffer(512);
            msg.write(buffer);
            this.time = System.currentTimeMillis();
            e.getChannel().write(buffer);
        }
}
