class BackupThread extends Thread {
    public java.util.Set<Channel> getChannels() {
        return new LinkedHashSet<Channel>(channels.values());
    }
}
