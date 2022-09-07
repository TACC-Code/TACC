class BackupThread extends Thread {
    public Enumeration getChannels() {
        return _channels.elements();
    }
}
