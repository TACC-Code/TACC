class BackupThread extends Thread {
    public Room[] getChannels() {
        return channels.values().toArray(new Room[channels.size()]);
    }
}
