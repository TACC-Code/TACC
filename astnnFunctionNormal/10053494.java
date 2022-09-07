class BackupThread extends Thread {
    public void publish(final ChannelSnapshot snapshot, final long machineId) throws StateStoreException {
        Timestamp time = snapshot.getTimestamp().getSQLTimestamp();
        try {
            getChannelSnapshotInsertStatement();
            CHANNEL_SNAPSHOT_INSERT.setLong(1, machineId);
            CHANNEL_SNAPSHOT_INSERT.setString(2, snapshot.getPV());
            CHANNEL_SNAPSHOT_INSERT.setTimestamp(3, time);
            Array valueArray = _databaseAdaptor.getArray(SGNL_VALUE_ARRAY_TYPE, _connection, snapshot.getValue());
            CHANNEL_SNAPSHOT_INSERT.setArray(4, valueArray);
            CHANNEL_SNAPSHOT_INSERT.setInt(5, snapshot.getStatus());
            CHANNEL_SNAPSHOT_INSERT.setInt(6, snapshot.getSeverity());
            CHANNEL_SNAPSHOT_INSERT.addBatch();
        } catch (SQLException exception) {
            throw new StateStoreException("Error publishing a channel snapshot.", exception);
        }
    }
}
