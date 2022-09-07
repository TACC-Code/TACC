class BackupThread extends Thread {
    public static void notifYPlayers(Long uid, String rawInput) {
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        channelService.sendMessage(new ChannelMessage(String.valueOf(uid), rawInput));
    }
}
