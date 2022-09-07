class BackupThread extends Thread {
    public void onChannelParted(Channel c, String msg) {
        List<KarmaListener> copy = new ArrayList<KarmaListener>();
        copy.addAll(listeners);
        for (KarmaListener l : copy) {
            if (l.getChannel() == c) {
                l.getChannel().removeListener(l);
                listeners.remove(l);
            }
        }
    }
}
