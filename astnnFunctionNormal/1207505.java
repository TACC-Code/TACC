class BackupThread extends Thread {
    protected boolean publish(final Connection connection, final DatabaseAdaptor databaseAdaptor, final MachineSnapshot machineSnapshot) {
        try {
            final ChannelSnapshotTable channelSnapshotTable = getChannelSnapshotTable(connection, machineSnapshot);
            MACHINE_SNAPSHOT_TABLE.insert(connection, databaseAdaptor, channelSnapshotTable, machineSnapshot);
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
