class BackupThread extends Thread {
            public void handleMessage(IRCMessageEvent e) {
                String channel = NosuchnickError.getNickname(e.getMessage());
                if (getChannel(channel) != null) {
                    forwardMessage(e, channel);
                    e.consume();
                    fireMessageProcessedEvent(e.getMessage());
                }
            }
}
