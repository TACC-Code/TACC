class BackupThread extends Thread {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        log.debug("Received message from " + e.getChannel().getId());
        try {
            ChannelBuffer channelBuffer = (ChannelBuffer) e.getMessage();
            byte[] bytes = channelBuffer.array();
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            while (in.available() > 1) {
                RougeObject resp = BDecoder.bDecode(in);
                if (resp.hasKey("command") | resp.hasKey("payload")) {
                    String command = resp.getString("command");
                    RougeObject payload = resp.getRougeObject("payload");
                    this.onMessageReceived(e.getChannel(), command, payload);
                } else {
                    log.error("Missing command or payload in " + new String(bytes));
                }
            }
        } catch (Exception ex) {
            log.error("Error processing message " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
