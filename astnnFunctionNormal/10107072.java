class BackupThread extends Thread {
    public String getChannelName(String key) {
        return channelNames.get(key);
    }
}
