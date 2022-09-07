class BackupThread extends Thread {
    public String getChannelSourceID(final int index) {
        final ChannelWrapper wrapper = CHANNEL_WRAPPERS.get(index);
        return wrapper != null ? wrapper.getChannelSourceID() : null;
    }
}
