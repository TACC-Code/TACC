class BackupThread extends Thread {
    public int processAudio(AudioBuffer buffer) {
        if (vars.isBypassed()) return AUDIO_OK;
        final int f = vars.getFrequency();
        final int sr = (int) buffer.getSampleRate();
        boolean update = false;
        if (freq != f) {
            freq = f;
            update = true;
        }
        if (sampleRate != sr) {
            sampleRate = sr;
            update = true;
        }
        if (update) {
            sine = createOscillator(freq, sampleRate);
        }
        buffer.setChannelFormat(ChannelFormat.MONO);
        final int ns = buffer.getSampleCount();
        float[] samples = buffer.getChannel(0);
        for (int i = 0; i < ns; i++) {
            samples[i] = sine.out() * 0.1f;
        }
        return AUDIO_OK;
    }
}
