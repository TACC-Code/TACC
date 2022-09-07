class BackupThread extends Thread {
    public MachineSnapshot loadChannelSnapshotsInto(final Connection connection, final MachineSnapshot machineSnapshot) throws SQLException {
        final ChannelSnapshotTable channelSnapshotTable = getChannelSnapshotTable(connection, machineSnapshot);
        return MACHINE_SNAPSHOT_TABLE.loadChannelSnapshotsInto(connection, channelSnapshotTable, machineSnapshot);
    }
}
