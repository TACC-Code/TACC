class BackupThread extends Thread {
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            Object obj = e.getMessage();
            if (obj instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) obj;
                if (response.getStatus().getCode() != 200) {
                    logger.error("Pull/Push received an error response:" + response);
                    if (null != response.getContent()) {
                        int k = response.getContent().readableBytes();
                        byte[] b = new byte[k];
                        response.getContent().readBytes(b);
                        logger.error("####Error cause:" + new String(b));
                    }
                    e.getChannel().close();
                    return;
                }
                if (!response.isChunked()) {
                    fullResponseReceived();
                }
            } else if (obj instanceof HttpChunk) {
                HttpChunk chunk = (HttpChunk) obj;
                if (chunk.isLast()) {
                    fullResponseReceived();
                } else {
                    ChannelBuffer content = chunk.getContent();
                    byte[] tmp = new byte[content.readableBytes()];
                    content.readBytes(tmp);
                    if (null != cumulation && cumulation.readable()) {
                        cumulation.write(tmp);
                        cumulation.discardReadedBytes();
                    } else {
                        cumulation = Buffer.wrapReadableContent(tmp);
                    }
                    if (cumulation.readableBytes() > 4) {
                        int size = BufferHelper.readFixInt32(cumulation, true);
                        if (size <= cumulation.readableBytes()) {
                            Buffer ck = Buffer.wrapReadableContent(cumulation.getRawBuffer(), cumulation.getReadIndex(), size);
                            if (logger.isDebugEnabled()) {
                                logger.debug("Handle " + size + " chunk.");
                            }
                            conn.handleReceivedContent(ck);
                            cumulation.advanceReadIndex(size);
                        } else {
                            cumulation.advanceReadIndex(-4);
                        }
                    }
                }
            }
        }
}
