class BackupThread extends Thread {
    public static String getChannel(IRCMessage msg) {
        return (String) msg.getArgs().elementAt(1);
    }
}
