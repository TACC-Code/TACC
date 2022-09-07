class BackupThread extends Thread {
    public void createNamedCommunicationChannel(String groupName, String channelName, Member member) {
        String channelID = this.getChannelID(groupName, channelName);
        if (!this.commChannels.containsKey(channelID)) {
            Workgroup workgroup = this.joinedWorkgroups.get(groupName).get(member);
            CommunicationChannelImpl channel = new CommunicationChannelImpl(channelID, (WorkgroupImpl) workgroup);
            HashMap<Member, CommunicationChannelImpl> map = new HashMap<Member, CommunicationChannelImpl>();
            map.put(member, channel);
            this.commChannels.put(channelID, map);
            this.applicationMessageListeners.put(channelID, new HashMap<Member, LinkedList<ApplicationMessageListener>>());
        } else {
            Workgroup workgroup = this.joinedWorkgroups.get(groupName).get(member);
            CommunicationChannelImpl channel = new CommunicationChannelImpl(channelID, (WorkgroupImpl) workgroup);
            this.commChannels.get(channelID).put(member, channel);
        }
    }
}
