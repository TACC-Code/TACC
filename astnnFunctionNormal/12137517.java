class BackupThread extends Thread {
    public AbstractIRCChannel getChannel(String name) {
        return this.channels.get(name);
    }
}
