class BackupThread extends Thread {
    public void printToVisibleChatbox(String name, String message, ChatStyles modeStyle, boolean popupEnabled) {
        Component tc = tabpn_tabs.getSelectedComponent();
        if (tc == null || tc instanceof LoginPanel) {
            if (popupEnabled) {
                JOptionPane.showMessageDialog(Globals.getClientFrame(), message, "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (tc instanceof ChannelPanel) {
            ChannelPanel cp = (ChannelPanel) tc;
            cp.printMainChatMessage(name, message, modeStyle);
        } else if (tc instanceof RoomPanel) {
            RoomPanel rp = (RoomPanel) tc;
            rp.chat(name, message, modeStyle);
        } else if (tc instanceof PrivateChatPanel) {
            PrivateChatPanel pcp = (PrivateChatPanel) tc;
            pcp.append(name, message, modeStyle);
        } else {
            ChannelPanel cp = TabOrganizer.getChannelPanel(0);
            if (cp != null) {
                cp.printMainChatMessage(name, message, modeStyle);
            }
        }
    }
}
