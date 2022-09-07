class BackupThread extends Thread {
    public void run() {
        runInit();
        int ch;
        SpectStreamSlot runInSlot;
        SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        SpectStream runOutStream = null;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        int zoomFactor;
        int srcBands, destBands;
        int startBand;
        topLevel: try {
            runInSlot = (SpectStreamSlot) slots.elementAt(SLOT_INPUT);
            if (runInSlot.getLinked() == null) {
                runStop();
            }
            for (boolean initDone = false; !initDone && !threadDead; ) {
                try {
                    runInStream = runInSlot.getDescr();
                    initDone = true;
                } catch (InterruptedException e) {
                }
                runCheckPause();
            }
            if (threadDead) break topLevel;
            zoomFactor = 2 << pr.intg[PR_FACTOR];
            srcBands = runInStream.bands;
            destBands = (srcBands - 1) / zoomFactor + 1;
            runOutSlot = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT);
            runOutStream = new SpectStream(runInStream);
            runOutStream.bands = destBands;
            runOutStream.smpPerFrame /= zoomFactor;
            runOutSlot.initWriter(runOutStream);
            startBand = (int) (pr.para[PR_BAND].val / ((runInStream.hiFreq - runInStream.loFreq) / srcBands) + 0.5);
            startBand = startBand - (destBands >> 1);
            if (startBand < 0) {
                startBand = 0;
            } else if ((startBand + destBands) > srcBands) {
                startBand = srcBands - destBands;
            }
            runSlotsReady();
            mainLoop: while (!threadDead) {
                for (boolean readDone = false; (readDone == false) && !threadDead; ) {
                    try {
                        runInFr = runInSlot.readFrame();
                        readDone = true;
                        runOutFr = runOutStream.allocFrame();
                    } catch (InterruptedException e) {
                    } catch (EOFException e) {
                        break mainLoop;
                    }
                    runCheckPause();
                }
                if (threadDead) break mainLoop;
                for (ch = 0; ch < runOutStream.chanNum; ch++) {
                    System.arraycopy(runInFr.data[ch], startBand << 1, runOutFr.data[ch], 0, destBands << 1);
                }
                runInSlot.freeFrame(runInFr);
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
            runInStream.closeReader();
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
