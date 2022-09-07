class BackupThread extends Thread {
    public Channel getChannel(String signalName) {
        Channel channel;
        if (!channelMap.containsKey(signalName)) {
            channel = newChannel(signalName);
            channelMap.put(signalName, channel);
        } else {
            channel = channelMap.get(signalName);
        }
        return channel;
    }
}
