class BackupThread extends Thread {
    public List getChannelUsers() {
        return Collections.unmodifiableList(channelUsers);
    }
}
