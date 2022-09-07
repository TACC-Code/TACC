class BackupThread extends Thread {
    private void assertChannelLevel(final int expectedLevel, final int cueIndex, final int channelIndex) {
        LightCueDetail detail = getDetail(cueIndex);
        int level = detail.getChannelLevel(channelIndex).getChannelIntValue();
        assertEquals(level, expectedLevel);
    }
}
