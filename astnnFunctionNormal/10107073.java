class BackupThread extends Thread {
    public String getAvailablePublicChannel(int idPlayer) {
        int index = 1;
        while (true) {
            Set<Integer> channel = getChannelPlayers(PUBLIC_CHANNEL_PREFIX + index);
            if (channel.size() >= MAX_PLAYERS_PER_CHANNEL) index++; else return PUBLIC_CHANNEL_PREFIX + index;
        }
    }
}
