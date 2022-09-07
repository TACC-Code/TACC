class BackupThread extends Thread {
    public void run(Connection source, BNetUser user, String param, String[] params, boolean whisperBack, Account commanderAccount, boolean superUser) throws Exception {
        String channel = source.getChannel();
        source.sendLeaveChat();
        source.sendJoinChannel(channel);
    }
}
