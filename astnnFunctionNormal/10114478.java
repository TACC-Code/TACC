class BackupThread extends Thread {
    public void join() {
        server.sendRaw("JOIN :" + name);
        server.sendRaw("MODE " + name);
        isRunning = true;
        if (!server.getChannels().containsValue(this)) {
            server.getChannels().put(name, this);
        }
        future = server.getChanExec().scheduleWithFixedDelay(this, 0, 2, TimeUnit.MINUTES);
    }
}
