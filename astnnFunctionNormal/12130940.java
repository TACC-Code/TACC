class BackupThread extends Thread {
    public int processAudio(AudioBuffer buffer) {
        cacheVariables();
        buffer.monoToStereo();
        float[] samplesL = buffer.getChannel(0);
        float[] samplesR = buffer.getChannel(1);
        int ns = buffer.getSampleCount();
        for (int i = 0; i < ns; i++) {
            reverb(samplesL[i], samplesR[i]);
            samplesL[i] = left();
            samplesR[i] = right();
        }
        return AUDIO_OK;
    }
}
