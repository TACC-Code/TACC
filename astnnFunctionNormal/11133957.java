class BackupThread extends Thread {
    public float outA(int chan, float delay) {
        int ns = getSampleCount();
        float[] samples = getChannel(chan);
        int d1 = (int) delay;
        float w = delay - d1;
        int p1 = readIndex - d1;
        if (p1 < 0) p1 += ns;
        int p2 = readIndex - d1 - 1;
        if (p2 < 0) p2 += ns;
        return apzm1[chan] = samples[p2] + (samples[p1] - apzm1[chan]) * ((1 - w) / (1 + w));
    }
}
