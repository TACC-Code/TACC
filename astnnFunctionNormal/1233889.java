class BackupThread extends Thread {
    public void startTrigger() {
        if (cf == null) cf = ChannelFactory.defaultFactory();
        chan = cf.getChannel(triggerName);
        chan.addConnectionListener(this);
        chan.requestConnection();
    }
}
