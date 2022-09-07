class BackupThread extends Thread {
    private boolean hasAccess(String user, String targetNetwork, String channel, String privilege) {
        int channelNum;
        try {
            channelNum = getChannelNum(targetNetwork, channel);
        } catch (BotSecurityException e) {
            return false;
        }
        if (channelNum < 0) return false;
        return hasAccess(user, channelNum, privilege);
    }
}
