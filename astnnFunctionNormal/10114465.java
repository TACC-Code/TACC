class BackupThread extends Thread {
    public Channel(final String name, final Server server) {
        this.name = name;
        this.server = server;
        this.nick = server.getNick();
        this.server.getEventManager().addListener(this);
        this.server.getChannels().put(name, this);
        chanMode = new Mode(server, "");
    }
}
