class BackupThread extends Thread {
    public GMChannelRoute getRoute(int channelId) {
        Iterator channelIt = this.midiChannels.iterator();
        while (channelIt.hasNext()) {
            GMChannelRoute midiChannel = (GMChannelRoute) channelIt.next();
            if (midiChannel.getChannelId() == channelId) {
                return midiChannel;
            }
        }
        return null;
    }
}
