class BackupThread extends Thread {
    protected ChannelSnapshotTable getChannelSnapshotTable(final Connection connection, final MachineSnapshot machineSnapshot) throws SQLException {
        final String groupID = machineSnapshot.getType();
        final ChannelGroup group = getChannelGroup(connection, groupID);
        final String serviceID = group.getServiceID();
        return CHANNEL_SNAPSHOT_TABLES.get(serviceID);
    }
}
