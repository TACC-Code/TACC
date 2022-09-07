class BackupThread extends Thread {
    public void join(final String password) {
        server.sendRaw("JOIN :" + name + " " + password);
        server.sendRaw("MODE " + name);
        isRunning = true;
        if (!server.getChannels().containsValue(this)) {
            server.getChannels().put(name, this);
        }
        future = getServer().getChanExec().schedule(this, 5, TimeUnit.SECONDS);
    }
}
