class BackupThread extends Thread {
    public void printMainChatMessage(String channel, String name, String message, ChatStyles modeStyle) {
        ChannelPanel cp = TabOrganizer.getChannelPanel(channel);
        if (cp != null) {
            cp.printMainChatMessage(name, message, modeStyle);
            TabOrganizer.markTab(cp);
        }
    }
}
