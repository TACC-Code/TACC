class BackupThread extends Thread {
    public void onMessageReceived(Message m) {
        if (m instanceof UserMessage) {
            UserMessage message = (UserMessage) m;
            String cmd = message.getCommand();
            if (message.getUser().equals(this)) {
                if (cmd.equalsIgnoreCase("privmsg")) {
                    notifyMessageReceived(message);
                } else if (cmd.equalsIgnoreCase("join")) {
                    notifyJoined(message.getChannel());
                } else if (cmd.equalsIgnoreCase("part")) {
                    notifyParted(message.getChannel(), message.getText());
                } else if (cmd.equalsIgnoreCase("quit")) {
                    notifyQuit(message.getText());
                }
            }
        }
    }
}
