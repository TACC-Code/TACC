class BackupThread extends Thread {
    public List<NodeChannel> selectNodeChannels(String nodeId) {
        List<Channel> channels = selectChannels();
        List<NodeChannel> nodeChannels = new ArrayList<NodeChannel>(channels.size());
        List<NodeChannelControl> nodeChannelControls = selectNodeChannelControlsFor(nodeId);
        for (Channel channel : channels) {
            NodeChannelControl foundNodeChannelCtl = null;
            for (NodeChannelControl nodeChannelControl : nodeChannelControls) {
                if (nodeChannelControl.getChannelId().equals(channel.getChannelId())) {
                    foundNodeChannelCtl = nodeChannelControl;
                    break;
                }
            }
            nodeChannels.add(new NodeChannel(channel, foundNodeChannelCtl));
        }
        return nodeChannels;
    }
}
