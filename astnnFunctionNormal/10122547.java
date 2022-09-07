class BackupThread extends Thread {
    protected void doWork() {
        double fftOut[] = new double[fftsize * 2];
        double input[] = new double[fftsize];
        try {
            reader.seekEnvelopeStart(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int ch = reader.getChannels();
        int nRead = 0;
        AudioBuffer buffer = new AudioBuffer("TEMP", ch, chunksize, 44100);
        chunkPtr = 0;
        notifySizeObservers();
        chunkStartInSamples = 0;
        double maxV = 0.0;
        do {
            if (Thread.interrupted()) {
                return;
            }
            if (fftsize != chunksize) {
                for (int i = 0; i < fftsize - chunksize; i++) input[i] = input[i + chunksize];
            }
            buffer.makeSilence();
            try {
                reader.processAudio(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            nRead += chunksize;
            float left[] = buffer.getChannel(0);
            for (int i = fftsize - chunksize, j = 0; i < fftsize; i++, j++) {
                input[i] = left[j];
            }
            for (int i = 0; i < fftsize; i++) fftOut[i] = input[i];
            double spectrum[] = process.process(fftOut);
            client.process(spectrum, nBin);
            chunkPtr++;
            chunkStartInSamples += chunksize;
        } while (chunkPtr < sizeInChunks);
        System.out.println(" DATA BUILT maqxV " + maxV);
        notifyMoreDataObservers();
    }
}
