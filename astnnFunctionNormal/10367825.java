class BackupThread extends Thread {
    private void removePrivilege(String user, String privilege, String network, String channel) throws BotSecurityException {
        int channelNum;
        IRCLine line;
        channelNum = getChannelNum(network, channel);
        line = new IRCLine("");
        line.putBack(Integer.toString(channelNum));
        line.putBack(privilege);
        line.putBack(user);
        removeRow("useraccess", useraccessKey, line);
    }
}
