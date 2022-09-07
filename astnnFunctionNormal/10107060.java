class BackupThread extends Thread {
    public int getChannelOwner(String channel) {
        return channelOwners.get(Utilities.formatString(channel));
    }
}
