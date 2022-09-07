class BackupThread extends Thread {
    public static void addChannelListener(String channel, ChannelListener cl) {
        if (channelhandler == null) {
            channelListenerBuffer.add(new ChannelListenerBuffer(channel, cl));
            return;
        }
        channelhandler.getChannel(channel).addListener(cl);
    }
}
