class BackupThread extends Thread {
            public void handleMessage(IRCMessageEvent e) {
                ArrayList chans = JoinMessage.getChannels(e.getMessage());
                for (int i = 0; i < chans.size(); i++) {
                    forwardMessage(e, (String) chans.get(i));
                }
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
}
