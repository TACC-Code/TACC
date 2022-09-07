class BackupThread extends Thread {
    public int getChannelCount(final int totalChannelCount) {
        int channelCount = 0;
        for (int i = 0; i < totalChannelCount; i++) {
            if (includes(i)) {
                channelCount++;
            }
        }
        return channelCount;
    }
}
