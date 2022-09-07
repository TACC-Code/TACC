class BackupThread extends Thread {
    public void run() {
        runInit();
        int ch, i, j, k, m;
        double d1, d2;
        SpectStreamSlot[] runInSlot = new SpectStreamSlot[2];
        SpectStreamSlot runOutSlot;
        SpectStream[] runInStream = new SpectStream[2];
        SpectStream runOutStream = null;
        SpectFrame[] runInFr = new SpectFrame[2];
        SpectFrame runOutFr = null;
        int[] srcBands = new int[2];
        int fftSize, fullFFTsize, complexFFTsize;
        float[] convBuf1, convBuf2;
        float[][] fftBuf;
        float[] win;
        int readDone, oldReadDone;
        int[] phase = new int[2];
        topLevel: try {
            for (i = 0; i < 2; i++) {
                runInSlot[i] = (SpectStreamSlot) slots.elementAt(SLOT_INPUT1 + i);
                if (runInSlot[i].getLinked() == null) {
                    runStop();
                }
                for (boolean initDone = false; !initDone && !threadDead; ) {
                    try {
                        runInStream[i] = runInSlot[i].getDescr();
                        initDone = true;
                        srcBands[i] = runInStream[i].bands;
                    } catch (InterruptedException e) {
                    }
                    runCheckPause();
                }
            }
            if (threadDead) break topLevel;
            runOutSlot = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT);
            runOutStream = new SpectStream(runInStream[0]);
            runOutSlot.initWriter(runOutStream);
            fftSize = srcBands[0] - 1;
            fullFFTsize = fftSize << 1;
            complexFFTsize = fullFFTsize << 1;
            fftBuf = new float[2][complexFFTsize];
            win = new float[fullFFTsize];
            d1 = 1.0 / (double) fullFFTsize * Math.PI;
            for (i = 0; i < fullFFTsize; i++) {
                d2 = Math.cos(i * d1);
                win[i] = (float) (d2 * d2);
            }
            phase[0] = (int) (pr.para[PR_ANGLE].val / 100.0 * fullFFTsize + 0.5) % fullFFTsize;
            phase[1] = (phase[0] + fftSize) % fullFFTsize;
            runSlotsReady();
            mainLoop: while (!threadDead) {
                for (readDone = 0; (readDone < 2) && !threadDead; ) {
                    oldReadDone = readDone;
                    for (i = 0; i < 2; i++) {
                        try {
                            if (runInStream[i].framesReadable() > 0) {
                                runInFr[i] = runInSlot[i].readFrame();
                                readDone++;
                            }
                        } catch (InterruptedException e) {
                        } catch (EOFException e) {
                            break mainLoop;
                        }
                        runCheckPause();
                    }
                    if (oldReadDone == readDone) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }
                        runCheckPause();
                    }
                }
                if (threadDead) break mainLoop;
                runOutFr = runOutStream.allocFrame();
                for (ch = 0; ch < runOutStream.chanNum; ch++) {
                    for (k = 0; k < 2; k++) {
                        convBuf1 = runInFr[k].data[ch];
                        convBuf2 = fftBuf[k];
                        for (i = 0; i <= fullFFTsize; ) {
                            convBuf2[i] = (float) Math.log(Math.max(1.0e-24, convBuf1[i]));
                            i++;
                            convBuf2[i] = convBuf1[i];
                            i++;
                        }
                        for (i = fullFFTsize + 2, j = fullFFTsize - 2; i < complexFFTsize; j -= 2) {
                            convBuf2[i++] = convBuf2[j];
                            convBuf2[i++] = -convBuf2[j + 1];
                        }
                        Fourier.complexTransform(convBuf2, fullFFTsize, Fourier.INVERSE);
                        m = phase[k];
                        for (i = 0; m < fullFFTsize; ) {
                            convBuf2[i++] *= win[m];
                            convBuf2[i++] *= win[m++];
                        }
                        for (m = 0; i < complexFFTsize; ) {
                            convBuf2[i++] *= win[m];
                            convBuf2[i++] *= win[m++];
                        }
                    }
                    convBuf1 = fftBuf[0];
                    convBuf2 = runOutFr.data[ch];
                    Util.add(fftBuf[1], 0, convBuf1, 0, complexFFTsize);
                    Fourier.complexTransform(convBuf1, fullFFTsize, Fourier.FORWARD);
                    for (i = 0; i <= fullFFTsize; ) {
                        convBuf2[i] = (float) Math.exp(convBuf1[i]);
                        i++;
                        convBuf2[i] = convBuf1[i];
                        i++;
                    }
                }
                runInSlot[0].freeFrame(runInFr[0]);
                runInSlot[1].freeFrame(runInFr[1]);
                for (boolean writeDone = false; (writeDone == false) && !threadDead; ) {
                    try {
                        runOutSlot.writeFrame(runOutFr);
                        writeDone = true;
                        runFrameDone(runOutSlot, runOutFr);
                        runOutStream.freeFrame(runOutFr);
                    } catch (InterruptedException e) {
                    }
                    runCheckPause();
                }
            }
            runInStream[0].closeReader();
            runInStream[1].closeReader();
            runOutStream.closeWriter();
        } catch (IOException e) {
            runQuit(e);
            return;
        } catch (SlotAlreadyConnectedException e) {
            runQuit(e);
            return;
        }
        runQuit(null);
    }
}
