class BackupThread extends Thread {
    public int processAudio(AudioBuffer buffer) {
        boolean bypassed = vars.isBypassed();
        if (bypassed) {
            if (!wasBypassed) {
                if (delayBuffer != null) {
                    delayBuffer.makeSilence();
                }
                wasBypassed = true;
            }
            return AUDIO_OK;
        }
        float sampleRate = buffer.getSampleRate();
        int ns = buffer.getSampleCount();
        int nc = buffer.getChannelCount();
        float feedback = vars.getFeedback();
        float mix = vars.getMix();
        if (delayBuffer == null) {
            delayBuffer = new DelayBuffer(nc, msToSamples(vars.getMaxDelayMilliseconds(), sampleRate), sampleRate);
        } else {
            delayBuffer.conform(buffer);
        }
        if (tappedBuffer == null) {
            tappedBuffer = new DelayBuffer(nc, ns, sampleRate);
        } else {
            tappedBuffer.conform(buffer);
            if (tappedBuffer.getSampleCount() != ns) {
                tappedBuffer.changeSampleCount(ns, false);
            }
        }
        float ducking = vars.getDucking();
        if (ducking < 1f) {
            float square = buffer.square();
            meansquare += meanK * (square - meansquare);
            float rms = 10f * (float) FastMath.sqrt(meansquare);
            if (rms < ducking) {
            } else if (rms > 1f) {
                mix *= ducking;
            } else {
                mix *= ducking / rms;
            }
        }
        smoothedMix += 0.05f * (mix - smoothedMix);
        tappedBuffer.makeSilence();
        int delay = (int) msToSamples(60000 * vars.getDelayFactor() / bpm, sampleRate);
        for (int c = 0; c < nc; c++) {
            if (delay < ns) continue;
            delayBuffer.tap(c, tappedBuffer, delay, 1f);
        }
        delayBuffer.appendFiltered(buffer, tappedBuffer, feedback * 1.1f, vars.getLowpassCoefficient());
        for (int c = 0; c < nc; c++) {
            float[] samples = buffer.getChannel(c);
            float[] tapped = tappedBuffer.getChannel(c);
            for (int i = 0; i < ns; i++) {
                samples[i] += smoothedMix * tapped[i];
            }
        }
        wasBypassed = bypassed;
        return AUDIO_OK;
    }
}
