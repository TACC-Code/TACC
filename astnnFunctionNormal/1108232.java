class BackupThread extends Thread {
    public ServerListener getChannel(String name) {
        ServerListener chan = null;
        Set keySet = channelTabs.keySet();
        for (Iterator i = keySet.iterator(); i.hasNext(); ) {
            chan = (ServerListener) i.next();
            if (chan.getName().equals(name)) {
                break;
            }
        }
        return chan;
    }
}
