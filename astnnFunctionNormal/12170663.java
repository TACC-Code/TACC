class BackupThread extends Thread {
    public void insert(final Connection connection, final DatabaseAdaptor databaseAdaptor, final ChannelSnapshotTable channelSnapshotTable, final MachineSnapshot machineSnapshot) throws SQLException {
        final long primaryKey = fetchNextPrimaryKey(connection);
        final String type = machineSnapshot.getType();
        final Timestamp timeStamp = new Timestamp(machineSnapshot.getTimestamp().getTime());
        final PreparedStatement insertStatement = getInsertStatement(connection);
        insertStatement.setLong(1, primaryKey);
        insertStatement.setTimestamp(2, timeStamp);
        insertStatement.setString(3, type);
        insertStatement.setString(4, machineSnapshot.getComment());
        insertStatement.executeUpdate();
        final ChannelSnapshot[] channelSnapshots = machineSnapshot.getChannelSnapshots();
        channelSnapshotTable.insert(connection, databaseAdaptor, channelSnapshots, primaryKey);
        connection.commit();
        machineSnapshot.setId(primaryKey);
    }
}
