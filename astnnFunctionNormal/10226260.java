class BackupThread extends Thread {
    public Channel getChannel(String name, boolean partial) {
        name = name.replaceFirst("#", "").toLowerCase();
        Channel channel = channelMap.get(name);
        if (channel == null && partial) {
            Iterator<String> names = channelMap.keySet().iterator();
            while (channel == null && names.hasNext()) {
                String name2 = names.next();
                if (name2.startsWith(name)) {
                    channel = channelMap.get(name2);
                }
            }
        }
        return channel;
    }
}
