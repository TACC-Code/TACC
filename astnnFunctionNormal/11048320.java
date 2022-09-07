class BackupThread extends Thread {
    public static void addChannelListener(int id, ChannelListener cl) {
        if (channelhandler == null) {
            channelListenerBuffer.add(new ChannelListenerBuffer(id + "_nb", cl));
            return;
        }
        channelhandler.getChannel(id).addListener(cl);
    }
}
