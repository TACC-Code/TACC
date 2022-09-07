class BackupThread extends Thread {
    public static void openRoomPanel(RoomData roomData) {
        if (roomPanel == null) {
            roomPanel = new RoomPanel(roomData);
            FrameOrganizer.closeJoinRoomPasswordFrame();
            tabHolder.insertTab(ROOM + GameDatabase.getShortName(roomData.getChannel()), null, roomPanel, roomData.getChannel(), channelPanels.size());
            tabHolder.setTabComponentAt(channelPanels.size(), new TabComponent(ROOM + GameDatabase.getShortName(roomData.getChannel()), Icons.lobbyIconSmall, roomPanel));
            tabHolder.setSelectedComponent(roomPanel);
            for (ChannelPanel cp : channelPanels) {
                cp.disableButtons();
            }
            roomPanel.initLauncher();
        } else {
            Logger.log(LogTypes.WARNING, "Close the current RoomPanel before opening a new one!");
        }
    }
}
