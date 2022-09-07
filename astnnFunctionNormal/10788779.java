class BackupThread extends Thread {
    public ChannelWrapper(String pv) {
        this(ChannelFactory.defaultFactory().getChannel(pv));
    }
}
