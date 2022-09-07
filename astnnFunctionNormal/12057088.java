class BackupThread extends Thread {
    public static synchronized ChannelFactory getChannelFactory() {
        if (channelFactory == null) {
            System.setProperty("gov.sns.jca.Context", "com.cosylab.epics.caj.CAJContext");
            channelFactory = ChannelFactory.defaultFactory();
            channelFactory.init();
        }
        return channelFactory;
    }
}
