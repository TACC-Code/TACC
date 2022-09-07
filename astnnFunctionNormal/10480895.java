class BackupThread extends Thread {
    public void process(final int startDimmerId, final int[] channelIds) {
        Dimmers dimmers = context.getShow().getDimmers();
        Channels channels = context.getShow().getChannels();
        for (int i = 0; i < channelIds.length; i++) {
            Dimmer dimmer = dimmers.get(startDimmerId + i);
            int channelId = channelIds[i];
            dimmer.setLanboxChannelId(channelId);
            if (updateChannels) {
                if (channelId >= 0 && channelId < channels.size()) {
                    Channel channel = channels.get(channelIds[i]);
                    dimmer.setChannel(channel);
                } else {
                    dimmer.setChannel(null);
                }
            }
        }
        expectedResponses--;
        if (expectedResponses == 0) {
            listener.changed();
        }
    }
}
