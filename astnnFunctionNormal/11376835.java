class BackupThread extends Thread {
    public NodeChannel getNodeChannel(String channelId, String nodeId, boolean refreshExtractMillis) {
        List<NodeChannel> channels = getNodeChannels(nodeId, refreshExtractMillis);
        for (NodeChannel nodeChannel : channels) {
            if (nodeChannel.getChannelId().equals(channelId)) {
                return nodeChannel;
            }
        }
        return null;
    }
}
