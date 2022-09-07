class BackupThread extends Thread {
    public String getChannelSourceID() {
        return CHANNEL.channelName();
    }
}
