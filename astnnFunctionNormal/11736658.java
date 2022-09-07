class BackupThread extends Thread {
    public DistributedList(ChannelFactory cf, String host, int port) {
        this.cf = cf;
        cf.init();
        try {
            channel = cf.getChannel(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("dl channel = " + channel);
        theList = new TreeMap<Counter, Object>();
        listener = new Listener(channel, theList);
        listener.start();
    }
}
