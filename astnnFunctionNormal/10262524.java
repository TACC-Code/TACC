class BackupThread extends Thread {
    public String getChannelName() {
        String name = methods.interfaces.getComponent(INTERFACE_CLAN_CHAT, INTERFACE_CLAN_CHAT_CHANNEL_NAME).getText();
        return name.substring(name.lastIndexOf(58) + 2).replaceAll("<.+>", "");
    }
}
