class BackupThread extends Thread {
    private void updateState() {
        if (App.gui.getFocussedMainFrame() == null) {
            currentChannel = null;
            setEnabled(false);
            return;
        }
        JaicWainIRCChannel chan = null;
        Component c = App.gui.getFocussedMainFrame().getTabbedChannelContainer().getTabbedPane().getSelectedComponent();
        if (c instanceof IRCChatPanel) {
            chan = ((IRCChatPanel) c).getChannel();
        } else {
            currentChannel = null;
            setEnabled(false);
            return;
        }
        currentChannel = chan;
        setEnabled(true);
    }
}
