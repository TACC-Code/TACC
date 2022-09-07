class BackupThread extends Thread {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws Exception {
        ChannelBuffer buffer = (ChannelBuffer) event.getMessage();
        logger.info("[PROCESSING] data length: [{}]", buffer.readableBytes());
        logger.debug(Markers.DUMP, "data: \n{}", HexString.dump(buffer));
        IProcessor processor = getProcessor();
        byte[] requestBuf = new byte[buffer.readableBytes()];
        buffer.readBytes(requestBuf);
        byte[] byResp = processor.getResponse(requestBuf);
        if (byResp != null && byResp.length > 0) {
            ChannelBuffer out = ChannelBuffers.buffer(byResp.length);
            out.writeBytes(byResp);
            event.getChannel().write(out);
        }
    }
}
