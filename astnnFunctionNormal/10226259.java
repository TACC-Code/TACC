class BackupThread extends Thread {
    public Channel getChannel(String name) {
        return getChannel(name, false);
    }
}
