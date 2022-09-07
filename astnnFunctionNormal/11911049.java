class BackupThread extends Thread {
    public Iterator getChannels() {
        return channels.keySet().iterator();
    }
}
