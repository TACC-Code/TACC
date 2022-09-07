class BackupThread extends Thread {
    public static Channel getChannel(String channelId) {
        return channels.getChannel(channelId);
    }
}
