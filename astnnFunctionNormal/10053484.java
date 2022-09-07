class BackupThread extends Thread {
    protected PreparedStatement getChannelSnapshotInsertStatement() throws SQLException {
        if (CHANNEL_SNAPSHOT_INSERT == null) {
            CHANNEL_SNAPSHOT_INSERT = _connection.prepareStatement("INSERT INTO " + PV_SNAPSHOT_TABLE + "(" + PV_SNAPSHOT_MACHINE_SNAPSHOT_COL + ", " + PV_SNAPSHOT_PV_COL + ", " + PV_SNAPSHOT_TIMESTAMP_COL + ", " + PV_SNAPSHOT_VALUE_COL + ", " + PV_SNAPSHOT_STATUS_COL + ", " + PV_SNAPSHOT_SEVERITY_COL + ") VALUES (?, ?, ?, ?, ?, ?)");
        }
        return CHANNEL_SNAPSHOT_INSERT;
    }
}
