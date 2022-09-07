class BackupThread extends Thread {
    public Channel getChannel(String channelName) {
        return channelMap.get(channelName.toLowerCase());
    }
}
