class BackupThread extends Thread {
    public float out(int chan, float delay) {
        int ns = getSampleCount();
        float[] samples = getChannel(chan);
        int d1 = (int) delay;
        float w = delay - d1;
        int p1 = readIndex - d1;
        if (p1 < 0) p1 += ns;
        int p2 = readIndex - d1 - 1;
        if (p2 < 0) p2 += ns;
        return (samples[p1] * (1 - w)) + (samples[p2] * w);
    }
}
