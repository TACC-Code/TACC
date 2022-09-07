class BackupThread extends Thread {
    public void append(FloatSampleBuffer source) {
        conform(source);
        int count = source.getSampleCount();
        int count2 = 0;
        if ((writeIndex + count) > getSampleCount()) {
            count = getSampleCount() - writeIndex;
            count2 = source.getSampleCount() - count;
        }
        for (int ch = 0; ch < source.getChannelCount(); ch++) {
            System.arraycopy(source.getChannel(ch), 0, getChannel(ch), writeIndex, count);
        }
        if (count2 > 0) for (int ch = 0; ch < source.getChannelCount(); ch++) {
            System.arraycopy(source.getChannel(ch), count, getChannel(ch), 0, count2);
        }
        nudge(source.getSampleCount());
    }
}
