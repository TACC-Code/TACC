class BackupThread extends Thread {
    public String getChannelOwner() {
        String owner = methods.interfaces.getComponent(INTERFACE_CLAN_CHAT, INTERFACE_CLAN_CHAT_CHANNEL_OWNER).getText();
        return owner.substring(owner.lastIndexOf(58) + 2).replaceAll("<.+>", "");
    }
}
