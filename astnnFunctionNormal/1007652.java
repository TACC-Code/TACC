class BackupThread extends Thread {
    public Channel[] getChannels() {
        return channels.values().toArray(new Channel[0]);
    }
}
