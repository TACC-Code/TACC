class BackupThread extends Thread {
    public Vector getChannels(final String groupType) {
        final LoggerSession session = _model.getLoggerSession(groupType);
        if (session != null) {
            final Collection channels = session.getChannels();
            Vector channelInfoList = new Vector(channels.size());
            for (Iterator iter = channels.iterator(); iter.hasNext(); ) {
                Hashtable info = new Hashtable();
                Channel channel = (Channel) iter.next();
                info.put(CHANNEL_SIGNAL, channel.channelName());
                info.put(CHANNEL_CONNECTED, new Boolean(channel.isConnected()));
                channelInfoList.add(info);
            }
            return channelInfoList;
        } else {
            return new Vector();
        }
    }
}
