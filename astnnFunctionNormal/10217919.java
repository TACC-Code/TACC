class BackupThread extends Thread {
    ChannelFactory getChannelFactoryForContext(Context context) throws NamingException {
        String key = context.getNameInNamespace();
        synchronized (guard) {
            if (containsChannelFactory(key)) {
                return (ChannelFactory) contextNamesToChannelFactories.get(key);
            } else {
                ChannelFactory channelFactory = new MultiplexChannelFactory();
                contextNamesToChannelFactories.put(key, channelFactory);
                SomniLogger.IT.config("Added " + key + " ChannelFactory.");
                return channelFactory;
            }
        }
    }
}
