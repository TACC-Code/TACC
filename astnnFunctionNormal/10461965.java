class BackupThread extends Thread {
    public void setChannelName(String chanName) {
        chName = chanName;
        if (chanName != null) {
            Channel chIn = ChannelFactory.defaultFactory().getChannel(chanName);
            stopMonitor();
            tryConnect(chIn);
        } else {
            stopMonitor();
            tryConnect(null);
        }
    }
}
