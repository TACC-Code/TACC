class BackupThread extends Thread {
    public DistHashTable(ChannelFactory cf, String host, int port) {
        this.cf = cf;
        cf.init();
        try {
            channel = cf.getChannel(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("dl channel = " + channel);
        }
        theList = new TreeMap<K, V>();
        listener = new DHTListener<K, V>(channel, theList);
        synchronized (theList) {
            listener.start();
        }
    }
}
