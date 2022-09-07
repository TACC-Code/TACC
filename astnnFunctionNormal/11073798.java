class BackupThread extends Thread {
    public int getChannelPlaceholderCount() {
        return CHANNEL_WRAPPERS.size();
    }
}
