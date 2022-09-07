class BackupThread extends Thread {
    private ChannelWrapper getChannelWrapper(final int index) {
        return CHANNEL_WRAPPERS.get(index);
    }
}
