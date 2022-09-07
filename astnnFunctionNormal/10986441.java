class BackupThread extends Thread {
    public void onDisconnected(Server server) {
        List<KarmaListener> copy = new ArrayList<KarmaListener>();
        copy.addAll(listeners);
        for (KarmaListener l : copy) {
            l.getChannel().removeListener(l);
            listeners.remove(l);
        }
    }
}
