class BackupThread extends Thread {
    public ChannelMap getSuspendIgnoreChannelLists(final String nodeId) {
        ChannelMap map = new ChannelMap();
        List<NodeChannel> ncs = getNodeChannels(nodeId, true);
        if (ncs != null) {
            for (NodeChannel nc : ncs) {
                if (nc.isSuspendEnabled()) {
                    map.addSuspendChannels(nc.getChannelId());
                }
                if (nc.isIgnoreEnabled()) {
                    map.addIgnoreChannels(nc.getChannelId());
                }
            }
        }
        return map;
    }
}
