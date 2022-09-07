class BackupThread extends Thread {
    public static void closeRoomPanel() {
        if (roomPanel != null) {
            SoundPlayer.playRoomCloseSound();
            tabHolder.remove(roomPanel);
            int index = tabHolder.indexOfTab(roomPanel.getRoomData().getChannel());
            if (index != -1) {
                tabHolder.setSelectedIndex(index);
            }
            if (roomPanel.isHost()) {
                Hotkeys.unbindHotKey(Hotkeys.ACTION_LAUNCH);
            }
            roomPanel = null;
            if (!Launcher.isPlaying()) {
                Launcher.deInitialize();
            }
            FrameOrganizer.closeGameSettingsFrame();
            for (ChannelPanel cp : channelPanels) {
                cp.enableButtons();
            }
        }
    }
}
