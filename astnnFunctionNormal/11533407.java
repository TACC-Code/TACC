class BackupThread extends Thread {
    public int getChannelIndex(final String channelID) {
        for (int i = 0; i < channels.size(); i++) {
            Channel ch = (Channel) channels.get(i);
            if (channelID.equals(ch.channelID)) {
                return i;
            }
        }
        return -1;
    }
}
