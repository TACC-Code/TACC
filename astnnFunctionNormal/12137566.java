class BackupThread extends Thread {
            public void handleMessage(IRCMessageEvent e) {
                String channel = EndofnamesReply.getChannel(e.getMessage());
                if (getChannel(channel) != null) {
                    forwardMessage(e, channel);
                    e.consume();
                    fireMessageProcessedEvent(e.getMessage());
                }
            }
}
