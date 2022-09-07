class BackupThread extends Thread {
    protected ChannelSnapshot[] fetchChannelSnapshotsForMachineSP(final long id) throws StateStoreException {
        try {
            List<ChannelSnapshot> snapshots = new ArrayList<ChannelSnapshot>();
            getChannelSnapshotQueryByMachineSnapshotStatement();
            CHANNEL_SNAPSHOT_QUERY_BY_MACHINE_SNAPSHOT.setLong(1, id);
            ResultSet snapshotResult = CHANNEL_SNAPSHOT_QUERY_BY_MACHINE_SNAPSHOT.executeQuery();
            commit();
            while (snapshotResult.next()) {
                String pv = snapshotResult.getString(PV_SNAPSHOT_PV_SP_COL);
                Timestamp timestamp = snapshotResult.getTimestamp(PV_SNAPSHOT_TIMESTAMP_COL);
                BigDecimal[] bigValue = (BigDecimal[]) snapshotResult.getArray(PV_SNAPSHOT_VALUE_SP_COL).getArray();
                double[] value = toDoubleArray(bigValue);
                short status = snapshotResult.getShort(PV_SNAPSHOT_STATUS_COL);
                short severity = snapshotResult.getShort(PV_SNAPSHOT_SEVERITY_COL);
                snapshots.add(new ChannelSnapshot(pv, value, status, severity, new gov.sns.ca.Timestamp(timestamp)));
            }
            return snapshots.toArray(new ChannelSnapshot[snapshots.size()]);
        } catch (SQLException exception) {
            throw new StateStoreException("Error fetching channel snapshots for the specified machine id.", exception);
        }
    }
}
