class BackupThread extends Thread {
    public void tap(int ch, FloatSampleBuffer buf, int delay, float weight) {
        if (weight < 0.001f) return;
        int sns = getSampleCount();
        float[] source = getChannel(ch);
        int dns = buf.getSampleCount();
        float[] dest = buf.getChannel(ch);
        int j = readIndex - delay;
        if (j < 0) j += sns;
        int count = Math.min(sns - j, dns);
        int i;
        for (i = 0; i < count; i++) {
            dest[i] += source[i + j] * weight;
        }
        j = -i;
        for (; i < dns; i++) {
            dest[i] += source[i + j] * weight;
        }
    }
}
