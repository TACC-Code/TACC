class BackupThread extends Thread {
    protected PreparedStatement getChannelSnapshotQueryByMachineSnapshotStatement() throws SQLException {
        if (CHANNEL_SNAPSHOT_QUERY_BY_MACHINE_SNAPSHOT == null) {
            CHANNEL_SNAPSHOT_QUERY_BY_MACHINE_SNAPSHOT = _connection.prepareStatement("SELECT * FROM " + PV_SNAPSHOT_TABLE + " WHERE " + PV_SNAPSHOT_MACHINE_SNAPSHOT_COL + " = ?");
        }
        return CHANNEL_SNAPSHOT_QUERY_BY_MACHINE_SNAPSHOT;
    }
}
