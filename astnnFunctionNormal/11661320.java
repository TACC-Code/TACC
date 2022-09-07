class BackupThread extends Thread {
    private void switchMixerId(String oldId, String newId) {
        ChannelList channels = mixer.getChannels();
        int oldIdCount = 0;
        int newIdCount = 0;
        for (int i = 0; i < arrangement.size(); i++) {
            String instrId = arrangement.getInstrumentAssignment(i).arrangementId;
            if (instrId.equals(oldId)) {
                oldIdCount++;
            } else if (instrId.equals(newId)) {
                newIdCount++;
            }
        }
        if (oldIdCount == 0 && newIdCount == 1) {
            for (int i = 0; i < channels.size(); i++) {
                Channel channel = channels.getChannel(i);
                if (channel.getName().equals(oldId)) {
                    channel.setName(newId);
                    break;
                }
            }
        } else if (oldIdCount == 0 && newIdCount > 1) {
            for (int i = 0; i < channels.size(); i++) {
                Channel channel = channels.getChannel(i);
                if (channel.getName().equals(oldId)) {
                    channels.removeChannel(channel);
                    break;
                }
            }
        } else if (oldIdCount > 0 && newIdCount == 1) {
            Channel channel = new Channel();
            channel.setName(newId);
            channels.addChannel(channel);
        }
    }
}
