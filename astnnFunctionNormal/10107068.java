class BackupThread extends Thread {
    public Set<Integer> getChannelPlayers(String channel) {
        String key = Utilities.formatString(channel);
        Set<Integer> channelPlayers = playersByChannel.get(key);
        if (channelPlayers == null) return new HashSet<Integer>();
        return channelPlayers;
    }
}
