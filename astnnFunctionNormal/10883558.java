class BackupThread extends Thread {
    public int getChannelsNotInGroupCount(final boolean any, final int groupIndex, final int totalChannelCount) {
        int channelCount = totalChannelCount;
        if (any) {
            channelCount -= getChannelCount(totalChannelCount);
        } else {
            if (groupIndex >= 0) {
                channelCount -= groups.get(groupIndex).size();
            }
        }
        return channelCount;
    }
}
