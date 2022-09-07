class BackupThread extends Thread {
    private void buildChannelTable() {
        channelTable = new Table(model.getChannelTableModel());
        channelTable.setName("channelTable");
        channelTable.setSelectionModel(model.getChannelSelectionModel());
        channelTable.setColumnWidth(0, 5);
        channelTable.setColumnPreferredWidth(1, 15);
        channelTable.setColumnWidth(2, 10);
        channelTable.setPreferredRowCount(20);
        channelTable.setShowGrid(false);
        channelTable.setDragEnabled(true);
        channelTable.setTransferHandler(new PatchChannelTransferHandler(channelTable, model));
        channelTable.setToolTipText(NLS.get("patch.channels.tooltip"));
        channelTable.setColumnToolTipText(0, NLS.get("patch.channels.column.number.tooltip"));
        channelTable.setColumnToolTipText(1, NLS.get("patch.channels.column.name.tooltip"));
    }
}
