class BackupThread extends Thread {
    private void takeSnapshot() {
        List<SnapshotValueConfig> snapshotValues = new ArrayList<SnapshotValueConfig>();
        for (Board currBoard : theLab.getAllBoardsFound()) {
            String currModule = currBoard.getBoardInstanceIdentifier();
            List<BoardSubchannelInfo> channelInfos = currBoard.getSnapshotChannels();
            if (channelInfos == null) {
                stdlog.severe("Module " + currModule + " does not provide channels to snapshot");
                continue;
            }
            for (BoardSubchannelInfo currInfo : channelInfos) {
                double value = currBoard.queryDoubleValue(currInfo.getSubchannel());
                SnapshotValueConfig newSnapshotValueConfig = new SnapshotValueConfig(currBoard.getCommChannel().getChannelName(), currBoard.getAddress(), currInfo.getModuleId(), currInfo.getSubchannel(), value, currInfo.isEnableEepromWriteBefore(), currInfo.getDependsOnSubchannel(), currInfo.getDescription());
                newSnapshotValueConfig.setReadonly(currInfo.isReadonly());
                snapshotValues.add(newSnapshotValueConfig);
            }
        }
        NameDialog dlg = new NameDialog(GlobalsLocator.getMainFrame(), GlobalsLocator.translate("enter_snapshotname"), true);
        dlg.pack();
        Ordering.centerDlgInFrame(GlobalsLocator.getMainFrame(), dlg);
        dlg.setVisible(true);
        if (dlg.isOKPressed()) {
            SnapshotConfig newSet = new SnapshotConfig();
            newSet.setName(dlg.getName());
            newSet.setSnapshotValues(snapshotValues);
            theLab.getConfig().getSnapshots().add(newSet);
            refreshTables();
        }
    }
}
