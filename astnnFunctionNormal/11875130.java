class BackupThread extends Thread {
    public Channel getChannel(String name, boolean create) {
        Channel c = (Channel) _channels.get(name.toLowerCase(java.util.Locale.ENGLISH));
        if ((c == null) && create) {
            c = new Channel(_ircConfiguration, name, this);
            _channels.put(name.toLowerCase(java.util.Locale.ENGLISH), c);
            _listeners.sendEvent("sourceCreated", c, this, new Boolean(true));
        }
        return c;
    }
}
