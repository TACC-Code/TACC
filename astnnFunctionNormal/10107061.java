class BackupThread extends Thread {
    public int getChannelMode(String channel) {
        return channelModes.get(Utilities.formatString(channel));
    }
}
