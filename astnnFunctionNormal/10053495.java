class BackupThread extends Thread {
    public void publish(final MachineSnapshot machineSnapshot) throws StateStoreException {
        if (machineSnapshot == null) {
            System.out.println("no data to be saved. skipping");
            return;
        }
        Timestamp time = new Timestamp(machineSnapshot.getTimestamp().getTime());
        try {
            getMachineSnapshotNextPrimaryKeyStatement();
            getMachineSnapshotInsertStatement();
            getChannelSnapshotInsertStatement();
            ResultSet idResult = MACHINE_SNAPSHOT_NEXT_PKEY.executeQuery();
            commit();
            idResult.next();
            long id = idResult.getLong(1);
            MACHINE_SNAPSHOT_INSERT.setLong(1, id);
            MACHINE_SNAPSHOT_INSERT.setTimestamp(2, time);
            MACHINE_SNAPSHOT_INSERT.setString(3, machineSnapshot.getType());
            MACHINE_SNAPSHOT_INSERT.setString(4, machineSnapshot.getComment());
            MACHINE_SNAPSHOT_INSERT.executeUpdate();
            commit();
            final ChannelSnapshot[] channelSnapshots = machineSnapshot.getChannelSnapshots();
            int count = 0;
            for (int index = 0; index < channelSnapshots.length; index++) {
                ChannelSnapshot channelSnapshot = channelSnapshots[index];
                if (channelSnapshot != null) {
                    publish(channelSnapshot, id);
                    count++;
                    if ((count % commitFrequency) == 0) {
                        CHANNEL_SNAPSHOT_INSERT.executeBatch();
                        commit();
                        CHANNEL_SNAPSHOT_INSERT.clearBatch();
                    }
                }
            }
            CHANNEL_SNAPSHOT_INSERT.executeBatch();
            commit();
            CHANNEL_SNAPSHOT_INSERT.clearBatch();
            machineSnapshot.setId(id);
        } catch (SQLException exception) {
            exception.getNextException().printStackTrace();
            throw new StateStoreException("Error publishing a machine snapshot.", exception);
        }
    }
}
