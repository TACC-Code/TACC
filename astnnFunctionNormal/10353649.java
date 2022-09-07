class BackupThread extends Thread {
    private void recalculate() {
        double sampleRate = m_audioFormat.getSampleRate();
        double bpm = getBeatsPerMinute();
        int beatsPerMeasure = getBeatsPerMeasure();
        int totalBeats = beatsPerMeasure * getMeasures();
        if (m_loop == null) {
            m_loop = new FloatSampleBuffer();
        }
        if (beatsPerMeasure <= 0 || getMeasures() <= 0 || bpm <= 0) {
            m_loop.reset(1, 0, (float) sampleRate);
            return;
        }
        double durationInSeconds = 60.0 / bpm * totalBeats;
        int sampleCount = (int) (sampleRate * durationInSeconds);
        if (Debug.getTracePlay()) Debug.out("Metronome.recalculate: sampleCount = " + sampleCount);
        m_loop.reset(1, sampleCount, (float) sampleRate);
        m_loop.makeSilence();
        float[] data = m_loop.getChannel(0);
        int tockDataLen = m_tock.getSampleCount();
        float[] tockData = m_tock.getChannel(0);
        for (int beat = 0; beat < totalBeats; beat++) {
            int samplePos = beat * sampleCount / totalBeats;
            float vol = VOLUME_OFFBEAT;
            if ((beat % beatsPerMeasure) == 0) {
                vol = VOLUME_DOWNBEAT;
                if (beat == 0) {
                    vol = VOLUME_FIRSTBEAT;
                }
            }
            mix(tockData, tockDataLen, data, samplePos, sampleCount, vol);
        }
    }
}
