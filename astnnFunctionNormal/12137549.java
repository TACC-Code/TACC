class BackupThread extends Thread {
            public void handleMessage(IRCMessageEvent e) {
                forwardMessage(e, KickMessage.getChannel(e.getMessage()));
                if (KickMessage.getUser(e.getMessage()).equals(user.getNickName())) {
                    removeChannel(KickMessage.getChannel(e.getMessage()));
                }
            }
}
