class BackupThread extends Thread {
    public Channel getChannel(final int index) {
        Channel channel = null;
        int channelOffset = 0;
        for (int i = 0; channel == null && i < groups.size(); i++) {
            Group group = groups.get(i);
            if (group.isEnabled()) {
                if (index < channelOffset + group.size()) {
                    int channelIndex = index - channelOffset;
                    channel = group.get(channelIndex);
                } else {
                    channelOffset += group.size();
                }
            }
        }
        if (channel == null) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + channelOffset);
        }
        return channel;
    }
}
