class BackupThread extends Thread {
    private void writeToStream(final ChannelGroup channelGroup, final RtmpMessage message) {
        if (message.getHeader().getChannelId() > 2) {
            message.getHeader().setStreamId(streamId);
        }
        channelGroup.write(message);
    }
}
