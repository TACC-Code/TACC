class BackupThread extends Thread {
    public final Object getChannelValue(String channelName) {
        return getChannel(channelName).getValue();
    }
}
