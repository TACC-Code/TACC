class BackupThread extends Thread {
    public void setChannelPV(final int index, final String channelPV) {
        if (channelPV != null && channelPV.length() > 0) {
            setChannel(index, ChannelFactory.defaultFactory().getChannel(channelPV));
        }
    }
}
