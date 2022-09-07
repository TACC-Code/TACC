class BackupThread extends Thread {
    public static String getChannelname(IRCMessage msg) {
        return msg.getArgs().get(0);
    }
}
