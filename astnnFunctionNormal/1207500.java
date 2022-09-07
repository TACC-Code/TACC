class BackupThread extends Thread {
    public ChannelGroup getChannelGroup(final Connection connection, final String type) throws SQLException {
        synchronized (CHANNEL_GROUPS) {
            if (!CHANNEL_GROUPS.containsKey(type)) {
                fetchChannelGroup(connection, type);
            }
            return CHANNEL_GROUPS.get(type);
        }
    }
}
