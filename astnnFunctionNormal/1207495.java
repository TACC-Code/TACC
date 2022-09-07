class BackupThread extends Thread {
    public MachineSnapshot fetchMachineSnapshot(final Connection connection, final long snapshotID) throws SQLException {
        final MachineSnapshot machineSnapshot = MACHINE_SNAPSHOT_TABLE.fetchMachineSnapshot(connection, snapshotID);
        final ChannelSnapshotTable channelSnapshotTable = getChannelSnapshotTable(connection, machineSnapshot);
        MACHINE_SNAPSHOT_TABLE.loadChannelSnapshotsInto(connection, channelSnapshotTable, machineSnapshot);
        return machineSnapshot;
    }
}
