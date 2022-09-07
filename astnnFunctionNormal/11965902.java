class BackupThread extends Thread {
        private void sendData(ChannelStateEvent e) {
            log.info("Sending data...");
            Channel channel = e.getChannel();
            if (channel.isWritable()) {
                ChannelBuffer buf = ChannelBufferHelper.buffer("hello!");
                channel.write(buf);
            }
        }
}
