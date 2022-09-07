class BackupThread extends Thread {
    public void setChannelNameQuietly(String chanName) {
        chName = chanName;
        if (chanName != null) {
            Channel chIn = ChannelFactory.defaultFactory().getChannel(chanName);
            ch = chIn;
        }
        stopMonitor();
    }
}
