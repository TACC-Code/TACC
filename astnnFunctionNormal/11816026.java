class BackupThread extends Thread {
        public int processAudio(AudioBuffer buffer) {
            if (master) {
                int ns = buffer.getSampleCount();
                if (ns != nsamples) {
                    vsti.turnOff();
                    nsamples = ns;
                    outSamples = new float[nOutChan][nsamples];
                    inSamples = new float[nInChan][nsamples];
                    vsti.setBlockSize(nsamples);
                    vsti.turnOn();
                }
                int sr = (int) buffer.getSampleRate();
                if (sr != sampleRate) {
                    sampleRate = sr;
                    vsti.setSampleRate(sampleRate);
                }
                if (mustClear) {
                    for (int i = 0; i < nOutChan; i++) {
                        Arrays.fill(outSamples[i], 0f);
                    }
                }
                vsti.processReplacing(inSamples, outSamples, nsamples);
            }
            buffer.setMetaInfo(info);
            buffer.setChannelFormat(format);
            for (int i = 0, j = startChan; i < nChans; i++, j++) {
                float[] from = outSamples[j];
                float[] to = buffer.getChannel(i);
                System.arraycopy(from, 0, to, 0, nsamples);
            }
            return AudioProcess.AUDIO_OK;
        }
}
