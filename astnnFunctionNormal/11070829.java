class BackupThread extends Thread {
    @Override
    public Iterator<TVCTChannel> getChannels() {
        return channels.iterator();
    }
}
