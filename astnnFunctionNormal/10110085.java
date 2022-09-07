class BackupThread extends Thread {
    public List<Channel> getChannels() {
        return Collections.unmodifiableList(new ArrayList<Channel>(channelMap.values()));
    }
}
