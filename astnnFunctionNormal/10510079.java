class BackupThread extends Thread {
    public int processAudio(AudioBuffer buffer) {
        boolean bypassed = vars.isBypassed();
        if (bypassed) {
            if (!wasBypassed) {
                clear();
            }
            wasBypassed = true;
            return AUDIO_OK;
        }
        wasBypassed = bypassed;
        int sr = (int) buffer.getSampleRate();
        if (sr != sampleRate) {
            sampleRate = sr;
            vars.update(sr);
        }
        cacheProcessVariables();
        float targetGain = 1f;
        float gain = makeupGain;
        int len = buffer.getSampleCount();
        int mslen = (int) (buffer.getSampleRate() / 1000);
        int nc = buffer.getChannelCount();
        for (int c = 0; c < nc; c++) {
            samples[c] = buffer.getChannel(c);
        }
        int nck;
        AudioBuffer keyBuffer = vars.getKeyBuffer();
        if (keyBuffer == null) {
            keySamples = samples;
            nck = nc;
        } else {
            nck = keyBuffer.getChannelCount();
            for (int c = 0; c < nck; c++) {
                tapSamples[c] = keyBuffer.getChannel(c);
            }
            keySamples = tapSamples;
        }
        float sample;
        for (int i = 0; i < len; i++) {
            float key = 0;
            if (isPeak) {
                for (int c = 0; c < nck; c++) {
                    key = max(key, abs(keySamples[c][i]));
                }
                targetGain = function(key);
            } else if ((i % mslen) == 0) {
                int length = (i + mslen) > len ? len - i : mslen;
                float sumOfSquares = 0f;
                for (int c = 0; c < nck; c++) {
                    for (int j = 0; j < length; j++) {
                        sample = keySamples[c][i + j];
                        sumOfSquares += sample * sample;
                    }
                }
                squaresums[nsqsum] = sumOfSquares / length;
                float mean = 0;
                for (int s = 0; s < NSQUARESUMS; s++) {
                    mean += squaresums[s];
                }
                if (++nsqsum >= NSQUARESUMS) nsqsum = 0;
                key = (float) sqrt(mean / NSQUARESUMS);
                targetGain = function(key);
            }
            gain = dynamics(targetGain);
            for (int c = 0; c < nc; c++) {
                samples[c][i] *= (gain * makeupGain) + dryGain;
            }
        }
        vars.setDynamicGain(gain);
        return AUDIO_OK;
    }
}
