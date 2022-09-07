class BackupThread extends Thread {
    public void run() {
        boolean wasHandled = false;
        if (getChatConsoleControllerSource() != null && getChatConsoleControllerSource() instanceof ChannelController) {
            getChatConsoleControllerSource().getConnector().setSpeakingChannelTells(((ChannelController) getChatConsoleControllerSource()).getChannel(), getChatConsoleControllerSource().isToolItemSelected(ToolBarItemKey.SPEAK_TELLS));
            wasHandled = true;
        }
        if (!wasHandled) {
            Raptor.getInstance().alert(getName() + " is only avalible from channel console sources.");
        }
    }
}
