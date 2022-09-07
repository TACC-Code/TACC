class BackupThread extends Thread {
    protected int routeDataForEachChannel() {
        Node sourceNode = nodeService.findIdentity();
        final List<NodeChannel> channels = configurationService.getNodeChannels(false);
        int dataCount = 0;
        for (NodeChannel nodeChannel : channels) {
            if (!nodeChannel.isSuspendEnabled() && nodeChannel.isEnabled()) {
                dataCount += routeDataForChannel(nodeChannel, sourceNode);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Not routing the {} channel.  It is either disabled or suspended.", nodeChannel.getChannelId());
                }
            }
        }
        return dataCount;
    }
}
