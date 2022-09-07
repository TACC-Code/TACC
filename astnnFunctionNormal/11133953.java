class BackupThread extends Thread {
    public void append(FloatSampleBuffer source1, FloatSampleBuffer source2, float level2) {
        conform(source1);
        int count = source1.getSampleCount();
        int count2 = 0;
        if ((writeIndex + count) >= getSampleCount()) {
            count = getSampleCount() - writeIndex;
            count2 = source1.getSampleCount() - count;
        }
        for (int ch = 0; ch < source1.getChannelCount(); ch++) {
            float[] dest = getChannel(ch);
            float[] src1 = source1.getChannel(ch);
            float[] src2 = source2.getChannel(ch);
            for (int i = 0; i < count; i++) {
                dest[i + writeIndex] = src1[i] + level2 * src2[i];
            }
        }
        if (count2 > 0) for (int ch = 0; ch < source1.getChannelCount(); ch++) {
            float[] dest = getChannel(ch);
            float[] src1 = source1.getChannel(ch);
            float[] src2 = source2.getChannel(ch);
            for (int i = 0, j = count; i < count2; i++, j++) {
                dest[i] = src1[j] + level2 * src2[j];
            }
        }
        nudge(source1.getSampleCount());
    }
}
