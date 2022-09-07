class BackupThread extends Thread {
    private void reconcileWithArrangement() {
        ChannelList channels = mixer.getChannels();
        ArrayList idList = new ArrayList();
        for (int i = 0; i < arrangement.size(); i++) {
            String instrId = arrangement.getInstrumentAssignment(i).arrangementId;
            if (!idList.contains(instrId)) {
                idList.add(instrId);
            }
        }
        for (int i = channels.size() - 1; i >= 0; i--) {
            Channel channel = channels.getChannel(i);
            if (!idList.contains(channel.getName())) {
                channels.removeChannel(channel);
            }
        }
        for (int i = 0; i < idList.size(); i++) {
            channels.checkOrCreate((String) idList.get(i));
        }
        channels.sort();
        channelsPanel.sort();
    }
}
