class BackupThread extends Thread {
    public CommunicationChannel getNamedCommunicationChannel(String groupName, String channelName, Member member) {
        return this.commChannels.get(this.getChannelID(groupName, channelName)).get(member);
    }
}
