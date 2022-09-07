class BackupThread extends Thread {
    public int processAudio(AudioBuffer buffer) {
        bypassed = controls.isBypassed();
        if (bypassed != wasBypassed) {
            if (bypassed) vstfx.turnOff(); else vstfx.turnOn();
            wasBypassed = bypassed;
        }
        if (bypassed) return AUDIO_OK;
        boolean mono = buffer.getChannelCount() == 1;
        buffer.monoToStereo();
        int ns = buffer.getSampleCount();
        if (ns != nsamples) {
            nsamples = ns;
            outSamples = new float[nOutChan][nsamples];
            inSamples = new float[nInChan][nsamples];
            vstfx.turnOff();
            vstfx.setBlockSize(nsamples);
            vstfx.turnOn();
        }
        int sr = (int) buffer.getSampleRate();
        if (sr != sampleRate) {
            sampleRate = sr;
            vstfx.turnOff();
            vstfx.setSampleRate(sampleRate);
            vstfx.turnOn();
        }
        if (mustClear) {
            for (int i = 0; i < nOutChan; i++) {
                Arrays.fill(outSamples[i], 0f);
            }
        }
        for (int i = 0; i < nInChan; i++) {
            float[] from = buffer.getChannel(i);
            float[] to = inSamples[i];
            System.arraycopy(from, 0, to, 0, nsamples);
        }
        vstfx.processReplacing(inSamples, outSamples, nsamples);
        for (int i = 0; i < nOutChan; i++) {
            float[] from = outSamples[i];
            float[] to = buffer.getChannel(i);
            System.arraycopy(from, 0, to, 0, nsamples);
        }
        if (mono) buffer.convertTo(ChannelFormat.MONO);
        return AUDIO_OK;
    }
}
