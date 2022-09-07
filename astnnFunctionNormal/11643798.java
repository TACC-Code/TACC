class BackupThread extends Thread {
    public int processAudio(AudioBuffer buffer) {
        if (vars.isBypassed()) return AUDIO_OK;
        int ns = buffer.getSampleCount();
        int nc = buffer.getChannelCount();
        for (int c = 0; c < nc; c++) {
            samples[c] = buffer.getChannel(c);
        }
        float _lfoInc = 2 * (float) Math.PI * (vars.getRate() / buffer.getSampleRate());
        float depth = vars.getDepth();
        float mod;
        for (int s = 0; s < ns; s++) {
            mod = 1 - depth * ((FastMath.sin(lfoPhase) + 1f) * 0.5f);
            lfoPhase += _lfoInc;
            if (lfoPhase >= Math.PI) lfoPhase -= Math.PI * 2;
            for (int c = 0; c < nc; c++) {
                samples[c][s] *= mod;
            }
        }
        return AUDIO_OK;
    }
}
