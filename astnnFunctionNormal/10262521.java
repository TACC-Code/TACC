class BackupThread extends Thread {
    public boolean leave() {
        methods.game.openTab(Game.TAB_CLAN);
        if (isInChannel()) {
            lastChannel = getChannelOwner();
            methods.interfaces.getComponent(INTERFACE_CLAN_CHAT, INTERFACE_CLAN_CHAT_BUTTON_JOIN).doClick();
            sleep(random(650, 900));
            return isInChannel();
        }
        return true;
    }
}
