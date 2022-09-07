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
        int sr = (int) buffer.getSampleRate();
        if (sr != sampleRate) {
            sampleRate = sr;
            vars.update(sr);
        }
        cacheProcessVariables();
        float targetGainM = 1f;
        float targetGainS = 1f;
        float gainM = 0f;
        float gainS = 0f;
        int len = buffer.getSampleCount();
        int mslen = (int) (buffer.getSampleRate() * 0.001f);
        float sumdiv = 1f / (mslen + mslen);
        if (!buffer.encodeMidSide()) return AUDIO_OK;
        samplesM = buffer.getChannel(0);
        samplesS = buffer.getChannel(1);
        for (int i = 0; i < len; i++) {
            float keyM = 0;
            float keyS = 0;
            if (isPeak) {
                keyM = max(keyM, abs(samplesM[i]));
                keyS = max(keyS, abs(samplesS[i]));
                targetGainM = function(0, keyM);
                targetGainS = function(1, keyS);
            } else if ((i % mslen) == 0 && (i + mslen) < len) {
                float sumOfSquaresM = 0f;
                float sumOfSquaresS = 0f;
                for (int j = 0, k = i; j < mslen; j++, k++) {
                    sumOfSquaresM += samplesM[k] * samplesM[k];
                    sumOfSquaresS += samplesS[k] * samplesS[k];
                }
                squaresumsM[nsqsum] = sumOfSquaresM * sumdiv;
                squaresumsS[nsqsum] = sumOfSquaresS * sumdiv;
                float meanM = 0;
                float meanS = 0;
                for (int s = 0; s < NSQUARESUMS; s++) {
                    meanM += squaresumsM[s];
                    meanS += squaresumsS[s];
                }
                if (++nsqsum >= NSQUARESUMS) nsqsum = 0;
                targetGainM = function(0, (float) sqrt(meanM / NSQUARESUMS));
                targetGainS = function(1, (float) sqrt(meanS / NSQUARESUMS));
            }
            gainM = dynamics(0, targetGainM);
            gainS = dynamics(1, targetGainS);
            samplesM[i] *= gainM * makeupGain[0];
            samplesS[i] *= gainS * makeupGain[1];
        }
        buffer.decodeMidSide();
        vars.setDynamicGain(gainM, gainS);
        wasBypassed = bypassed;
        return AUDIO_OK;
    }
}
