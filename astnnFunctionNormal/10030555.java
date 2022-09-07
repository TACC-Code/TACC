class BackupThread extends Thread {
    public static String getChannel(IRCMessage msg) {
        return msg.getArgs().get(0);
    }
}
