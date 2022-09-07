class BackupThread extends Thread {
    public double getMainFieldSetting(final Electromagnet magnet) {
        final Channel channel = magnet.getChannel(MagnetMainSupply.FIELD_SET_HANDLE);
        return _dataSource.getChannelSnapshotValue(channel.channelName())[0];
    }
}
