class BackupThread extends Thread {
            public void handleMessage(IRCMessageEvent e) {
                ArrayList chans = PartMessage.getChannels(e.getMessage());
                for (int i = 0; i < chans.size(); i++) {
                    forwardMessage(e, (String) chans.get(i));
                    if (e.getMessage().getNick().equals(user.getNickName())) {
                        removeChannel((String) chans.get(i));
                    }
                }
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
}
