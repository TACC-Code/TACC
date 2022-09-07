class BackupThread extends Thread {
            public void handleMessage(IRCMessageEvent e) {
                forwardMessage(e, TopicMessage.getChannel(e.getMessage()));
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
}
