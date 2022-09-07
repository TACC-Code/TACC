class BackupThread extends Thread {
    public void saveNodeChannelControl(NodeChannel nodeChannel, boolean reloadChannels) {
        if (0 == sqlTemplate.update(getSql("updateNodeChannelControlSql"), new Object[] { nodeChannel.isSuspendEnabled() ? 1 : 0, nodeChannel.isIgnoreEnabled() ? 1 : 0, nodeChannel.getLastExtractTime(), nodeChannel.getNodeId(), nodeChannel.getChannelId() })) {
            sqlTemplate.update(getSql("insertNodeChannelControlSql"), new Object[] { nodeChannel.getNodeId(), nodeChannel.getChannelId(), nodeChannel.isSuspendEnabled() ? 1 : 0, nodeChannel.isIgnoreEnabled() ? 1 : 0, nodeChannel.getLastExtractTime() });
        }
        if (reloadChannels) {
            reloadChannels();
        }
    }
}
