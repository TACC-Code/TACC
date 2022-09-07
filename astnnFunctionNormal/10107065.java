class BackupThread extends Thread {
    public void leaveChannel(String channel, int idPlayer) {
        String key = Utilities.formatString(channel);
        Set<Integer> channelPlayers = getChannelPlayers(channel);
        Set<String> playerChannels = getPlayerChannels(idPlayer);
        channelPlayers.remove(idPlayer);
        playerChannels.remove(key);
        if (channelPlayers.size() == 0) {
            playersByChannel.remove(key);
            channelNames.remove(key);
            channelOwners.remove(key);
        }
    }
}
