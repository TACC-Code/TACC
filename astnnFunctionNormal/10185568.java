class BackupThread extends Thread {
    private void writeToStream(final Channel channel, final RtmpMessage message) {
        if (message.getHeader().getChannelId() > 2) {
            message.getHeader().setStreamId(streamId);
        }
        channel.write(message);
    }
}
