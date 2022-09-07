class BackupThread extends Thread {
    public Channel getChannel(String name) {
        return channelhandler.getChannel(name);
    }
}
