class BackupThread extends Thread {
    protected void process() {
        int i, j, k, m, ch, off;
        int len, chunkLength;
        long progOff, progLen;
        float f1, f2, f3;
        double d1, d4;
        AudioFile inF = null;
        AudioFile nOutF = null;
        AudioFile tOutF = null;
        AudioFile eOutF = null;
        AudioFileDescr inStream = null;
        AudioFileDescr nOutStream = null;
        AudioFileDescr tOutStream = null;
        AudioFileDescr eOutStream = null;
        float[][] inBuf = null;
        float[][] nOutBuf = null;
        float[][] tOutBuf = null;
        float[][] eOutBuf = null;
        float[] convBuf1, fftBuf, weightBuf, window, wincorr, maxima;
        int[] maximaPos;
        int numMaxima;
        int inLength, inChanNum, outLength, outChanNum;
        int framesRead, framesWritten;
        int fftLength, frameLength, winStep;
        double totalTilt, totalNoise, totalEnergy;
        boolean outNoise, outTilt, outEnergy;
        MessageFormat msgForm;
        Object[] msgArgs = new Object[1];
        PathField ggOutput;
        topLevel: try {
            inF = AudioFile.openAsRead(new File(pr.text[PR_INPUTFILE]));
            inStream = inF.getDescr();
            inChanNum = inStream.channels;
            inLength = (int) inStream.length;
            if (inLength * inChanNum < 1) throw new EOFException(ERR_EMPTY);
            if (!threadRunning) break topLevel;
            outChanNum = inChanNum;
            outNoise = pr.bool[PR_OUTNOISE];
            outTilt = pr.bool[PR_OUTTILT];
            outEnergy = pr.bool[PR_OUTENERGY];
            if (!outNoise && !outTilt && !outEnergy) throw new IOException(ERR_NOOUTPUT);
            if (outNoise) {
                ggOutput = (PathField) gui.getItemObj(GG_NOUTPUTFILE);
                if (ggOutput == null) throw new IOException(ERR_MISSINGPROP);
                nOutStream = new AudioFileDescr(inStream);
                nOutStream.channels = outChanNum;
                ggOutput.fillStream(nOutStream);
                nOutF = AudioFile.openAsWrite(nOutStream);
                if (!threadRunning) break topLevel;
            }
            if (outTilt) {
                ggOutput = (PathField) gui.getItemObj(GG_TOUTPUTFILE);
                if (ggOutput == null) throw new IOException(ERR_MISSINGPROP);
                tOutStream = new AudioFileDescr(inStream);
                tOutStream.channels = outChanNum;
                ggOutput.fillStream(tOutStream);
                tOutF = AudioFile.openAsWrite(tOutStream);
                if (!threadRunning) break topLevel;
            }
            if (outEnergy) {
                ggOutput = (PathField) gui.getItemObj(GG_EOUTPUTFILE);
                if (ggOutput == null) throw new IOException(ERR_MISSINGPROP);
                eOutStream = new AudioFileDescr(inStream);
                eOutStream.channels = outChanNum;
                ggOutput.fillStream(eOutStream);
                eOutF = AudioFile.openAsWrite(eOutStream);
                if (!threadRunning) break topLevel;
            }
            frameLength = 32 << pr.intg[PR_FRAMESIZE];
            fftLength = frameLength << 1;
            winStep = frameLength >> pr.intg[PR_OVERLAP];
            numMaxima = frameLength >> 7;
            outLength = (inLength + winStep - 1) / winStep;
            inBuf = new float[inChanNum][frameLength];
            nOutBuf = new float[outChanNum][8];
            tOutBuf = new float[outChanNum][8];
            eOutBuf = new float[outChanNum][8];
            fftBuf = new float[fftLength + 2];
            window = Filter.createFullWindow(frameLength, Filter.WIN_HAMMING);
            wincorr = new float[frameLength];
            maxima = new float[numMaxima];
            maximaPos = new int[numMaxima];
            totalTilt = 0.0;
            totalNoise = 0.0;
            totalEnergy = 0.0;
            System.arraycopy(window, 0, fftBuf, 0, frameLength);
            for (i = frameLength; i < fftLength; ) {
                fftBuf[i++] = 0.0f;
            }
            Fourier.realTransform(fftBuf, fftLength, Fourier.FORWARD);
            d1 = 0.0;
            for (i = 0, k = 0; i <= fftLength; ) {
                j = i++;
                f1 = (fftBuf[j] * fftBuf[j] + fftBuf[i] * fftBuf[i]);
                fftBuf[j] = f1;
                d1 += f1;
                fftBuf[i++] = 0.0f;
            }
            Fourier.realTransform(fftBuf, fftLength, Fourier.INVERSE);
            Util.mult(fftBuf, 0, frameLength, 1.0f / fftBuf[0]);
            f1 = fftBuf[0];
            for (i = 1; i < frameLength; i++) {
                f1 = ((float) (frameLength - i) / (float) frameLength) / fftBuf[i];
                if (f1 > 500f) break;
                wincorr[i] = f1;
            }
            for (; i < frameLength; i++) {
                wincorr[i] = 500f;
            }
            weightBuf = new float[frameLength + 1];
            f1 = (float) inStream.rate / 2;
            for (i = 0; i <= frameLength; i++) {
                weightBuf[i] = (float) i / (float) frameLength * f1;
            }
            Filter.getDBAweights(weightBuf, weightBuf, frameLength + 1);
            for (i = 0; i <= frameLength; i++) {
                weightBuf[i] *= weightBuf[i];
            }
            progOff = 0;
            progLen = (long) inLength + (long) outLength;
            framesWritten = 0;
            framesRead = 0;
            off = 0;
            while (threadRunning && (framesWritten < outLength)) {
                len = Math.min(inLength - framesRead, frameLength - off);
                chunkLength = len + off;
                inF.readFrames(inBuf, off, len);
                framesRead += len;
                progOff += len;
                setProgression((float) progOff / (float) progLen);
                if (!threadRunning) break topLevel;
                if (chunkLength < frameLength) {
                    for (ch = 0; ch < inChanNum; ch++) {
                        convBuf1 = inBuf[ch];
                        for (i = chunkLength; i < frameLength; i++) {
                            convBuf1[i] = 0.0f;
                        }
                    }
                }
                for (ch = 0; ch < inChanNum; ch++) {
                    convBuf1 = inBuf[ch];
                    System.arraycopy(convBuf1, 0, fftBuf, 0, frameLength);
                    Util.mult(window, 0, fftBuf, 0, frameLength);
                    for (i = frameLength; i < fftLength; ) {
                        fftBuf[i++] = 0.0f;
                    }
                    Fourier.realTransform(fftBuf, fftLength, Fourier.FORWARD);
                    d1 = 0.0;
                    d4 = 0.0;
                    for (i = 0, k = 0; i <= fftLength; k++) {
                        j = i++;
                        f1 = (fftBuf[j] * fftBuf[j] + fftBuf[i] * fftBuf[i]) * weightBuf[k];
                        d4 += f1;
                        if (outTilt) {
                            d1 += f1 * k;
                        }
                        fftBuf[j] = f1;
                        fftBuf[i++] = 0.0f;
                    }
                    if (outTilt) {
                        if (d4 > 0.0) {
                            f1 = (float) (d1 / (d4 * frameLength));
                        } else {
                            f1 = 0.5f;
                        }
                        tOutBuf[ch][0] = f1;
                        totalTilt += f1;
                    }
                    if (outEnergy) {
                        f1 = Math.min(1.0f, (float) (Math.sqrt(d4) / frameLength));
                        eOutBuf[ch][0] = f1;
                        totalEnergy += f1;
                    }
                    if (outNoise) {
                        Fourier.realTransform(fftBuf, fftLength, Fourier.INVERSE);
                        f1 = fftBuf[0];
                        if (f1 > 0.0f) {
                            Util.mult(fftBuf, 0, frameLength, 1.0f / f1);
                            Util.mult(wincorr, 0, fftBuf, 0, frameLength);
                            Util.clear(maxima);
                            for (i = 0; i < numMaxima; i++) {
                                maximaPos[i] = frameLength;
                            }
                            fftBuf[fftLength] = 0f;
                            f2 = 1.1f;
                            d1 = 0.0;
                            for (i = 0; i < frameLength; i++) {
                                f3 = fftBuf[i];
                                if ((f3 > f2) && (f3 > fftBuf[i + 1]) && (f3 > 0.0f)) {
                                    if (f3 > maxima[0]) {
                                        for (j = 1; j < numMaxima; j++) {
                                            if (f3 <= maxima[j]) break;
                                        }
                                        j--;
                                        for (k = 0; k < j; k++) {
                                            maxima[k] = maxima[k + 1];
                                            maximaPos[k] = maximaPos[k + 1];
                                        }
                                        maxima[j] = f3;
                                        maximaPos[j] = i;
                                    }
                                }
                                f2 = f3;
                            }
                            d1 += maxima[0] / 2.0f * (frameLength - maximaPos[0]);
                            f1 = 1.0f;
                            j = 0;
                            do {
                                m = -1;
                                k = frameLength;
                                for (i = 0; i < numMaxima; i++) {
                                    if (maximaPos[i] < k) {
                                        k = maximaPos[i];
                                        m = i;
                                    }
                                }
                                if (m < 0) break;
                                d1 += (f1 + maxima[m]) / 2.0f * (maximaPos[m] - j);
                                j = maximaPos[m];
                                f1 = maxima[m];
                                maximaPos[m] = frameLength;
                            } while (true);
                            f1 = 1.13f - ((float) d1 * 2.1f / frameLength);
                            f1 = Math.min(1.0f, Math.max(0.0f, f1 * f1));
                        } else {
                            f1 = 0.0f;
                        }
                        nOutBuf[ch][0] = f1;
                        totalNoise += f1;
                    }
                }
                if (outTilt) {
                    tOutF.writeFrames(tOutBuf, 0, 1);
                }
                if (outNoise) {
                    nOutF.writeFrames(nOutBuf, 0, 1);
                }
                if (outEnergy) {
                    eOutF.writeFrames(eOutBuf, 0, 1);
                }
                framesWritten++;
                progOff++;
                setProgression((float) progOff / (float) progLen);
                if (!threadRunning) break topLevel;
                off = frameLength - winStep;
                for (ch = 0; ch < inChanNum; ch++) {
                    System.arraycopy(inBuf[ch], winStep, inBuf[ch], 0, off);
                }
            }
            if (!threadRunning) break topLevel;
            if (outNoise) {
                msgArgs[0] = new Double(totalNoise / (outLength * outChanNum) * 100);
                msgForm = new MessageFormat(PTRN_NOISE);
                msgForm.setLocale(Locale.US);
                msgForm.applyPattern(PTRN_NOISE);
                System.out.println(msgForm.format(msgArgs));
            }
            if (outTilt) {
                msgArgs[0] = new Double(totalTilt / (outLength * outChanNum) * (inStream.rate / 2));
                msgForm = new MessageFormat(PTRN_TILT);
                msgForm.setLocale(Locale.US);
                msgForm.applyPattern(PTRN_TILT);
                System.out.println(msgForm.format(msgArgs));
            }
            if (outEnergy) {
                msgArgs[0] = new Double(20 * Math.log(totalEnergy / (outLength * outChanNum)) / Constants.ln10);
                msgForm = new MessageFormat(PTRN_ENERGY);
                msgForm.setLocale(Locale.US);
                msgForm.applyPattern(PTRN_ENERGY);
                System.out.println(msgForm.format(msgArgs));
            }
            inF.close();
            inF = null;
            inStream = null;
            if (nOutF != null) {
                nOutF.close();
                nOutF = null;
            }
            if (tOutF != null) {
                tOutF.close();
                tOutF = null;
            }
            if (eOutF != null) {
                eOutF.close();
                eOutF = null;
            }
        } catch (IOException e1) {
            setError(e1);
        } catch (OutOfMemoryError e2) {
            inStream = null;
            nOutStream = null;
            tOutStream = null;
            inBuf = null;
            nOutBuf = null;
            tOutBuf = null;
            eOutBuf = null;
            fftBuf = null;
            weightBuf = null;
            window = null;
            wincorr = null;
            System.gc();
            setError(new Exception(ERR_MEMORY));
            ;
        }
        if (inF != null) {
            inF.cleanUp();
        }
        if (nOutF != null) {
            nOutF.cleanUp();
        }
        if (tOutF != null) {
            tOutF.cleanUp();
        }
        if (eOutF != null) {
            eOutF.cleanUp();
        }
    }
}
