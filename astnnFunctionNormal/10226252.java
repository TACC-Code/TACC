class BackupThread extends Thread {
    public void removeChannel(String name) {
        Channel channel = getChannel(name);
        removeChannel(channel);
        channel.getConfig().setPersistent(false);
        channel.send(new ShutdownMessage());
    }
}
