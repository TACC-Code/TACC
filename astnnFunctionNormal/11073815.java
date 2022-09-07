class BackupThread extends Thread {
    public boolean isChannelPlotting(final int index) {
        final ChannelWrapper wrapper = getChannelWrapper(index);
        return wrapper != null ? wrapper.isPlotting() : false;
    }
}
