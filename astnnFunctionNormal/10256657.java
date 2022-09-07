class BackupThread extends Thread {
    public int processAudio(AudioBuffer buffer) {
        if (controls.isBypassed()) return AUDIO_OK;
        int ns = buffer.getSampleCount();
        int nc = buffer.getChannelCount();
        int sc = ns * nc;
        int denorms = 0;
        float[] samples;
        for (int c = 0; c < nc; c++) {
            samples = buffer.getChannel(c);
            for (int i = 0; i < ns; i++) {
                if (isDenormal(samples[i])) {
                    samples[i] = 0f;
                    denorms++;
                }
            }
        }
        controls.setDenormFactor((float) denorms / sc);
        return AUDIO_OK;
    }
}
