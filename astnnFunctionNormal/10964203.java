class BackupThread extends Thread {
    protected void doWork() {
        running = true;
        abortFlag = false;
        chunkPtr = 0;
        chunkStartInSamples = 0;
        totalFramesRendered = 0;
        do {
            if (abortFlag) break;
            if (fftsize != chunksize) {
                for (int i = 0; i < fftsize - chunksize; i++) input[i] = input[i + chunksize];
            }
            buffer.makeSilence();
            reader.processAudio(buffer);
            float left[] = buffer.getChannel(0);
            for (int i = fftsize - chunksize, j = 0; i < fftsize; i++, j++) {
                input[i] = left[j];
            }
            if (chunkPtr < 0) {
                chunkPtr++;
                chunkStartInSamples += chunksize;
                continue;
            }
            fftWorker.process(input, fftBuffer[chunkPtr]);
            data[0] = magnArray[chunkPtr];
            fftMagn.getData(data, fftBuffer[chunkPtr]);
            notifyMoreDataObservers(magnArray[chunkPtr]);
            chunkPtr++;
            totalFramesRendered++;
            if (chunkPtr >= sizeInChunks) chunkPtr = 0;
        } while (true);
        running = false;
        abortFlag = false;
        if (abortWaiter != null) abortWaiter.interrupt();
        System.out.println(" ABORTED ");
    }
}
