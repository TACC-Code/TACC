class BackupThread extends Thread {
    public float outU(int chan, int delay) {
        int p = readIndex - delay;
        if (p < 0) p += getSampleCount();
        return getChannel(chan)[p];
    }
}
