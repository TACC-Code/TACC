class BackupThread extends Thread {
    public Room getChannel(String channelName) {
        return channels.get(channelName);
    }
}
