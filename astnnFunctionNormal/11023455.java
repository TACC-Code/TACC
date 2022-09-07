class BackupThread extends Thread {
    public static void sendServerShuttingDown() {
        for (Channel ch : ChannelData.getChannels().values()) {
            for (Player p : ch.getPlayersInChannel()) {
                new Message(p.getConnection(), ServerProtocolCommands.SERVER_SHUTTING_DOWN);
            }
        }
    }
}
