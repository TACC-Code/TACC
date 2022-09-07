class BackupThread extends Thread {
    public boolean isChannelEnabled(final int index) {
        final ChannelWrapper wrapper = getChannelWrapper(index);
        return wrapper != null ? wrapper.isEnabled() : false;
    }
}
