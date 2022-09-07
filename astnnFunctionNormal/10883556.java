class BackupThread extends Thread {
    public int[] getChannelsNotInGroup(final boolean any, final int groupIndex, final int totalChannelCount) {
        int channelCount = getChannelsNotInGroupCount(any, groupIndex, totalChannelCount);
        int[] indexes = new int[channelCount];
        if (any) {
            for (int i = 0, j = 0; i < totalChannelCount; i++) {
                if (!includes(i)) {
                    indexes[j++] = i;
                }
            }
        } else {
            if (groupIndex >= 0) {
                Group group = groups.get(groupIndex);
                for (int i = 0, j = 0; i < totalChannelCount; i++) {
                    if (!group.includes(i)) {
                        indexes[j++] = i;
                    }
                }
            } else {
                for (int i = 0; i < totalChannelCount; i++) {
                    indexes[i] = i;
                }
            }
        }
        return indexes;
    }
}
