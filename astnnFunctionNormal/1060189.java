class BackupThread extends Thread {
    protected void CavPhaseAvg() {
        noisecount = new int[myDoc.numberOfCav];
        beamcount = new int[myDoc.numberOfCav];
        signalPhase = new double[myDoc.numberOfCav];
        signalAmplitude = new double[myDoc.numberOfCav];
        noisePhase = new double[myDoc.numberOfCav];
        noiseAmplitude = new double[myDoc.numberOfCav];
        beamPhase = new double[myDoc.numberOfCav];
        beamAmplitude = new double[myDoc.numberOfCav];
        amin = new double[myDoc.numberOfCav];
        amax = new double[myDoc.numberOfCav];
        pmin = new double[myDoc.numberOfCav];
        pmax = new double[myDoc.numberOfCav];
        totalphase = new double[myDoc.numberOfCav];
        for (int k = 0; k < myDoc.numberOfCav; k++) {
            signalPhase[k] = 0.;
            signalAmplitude[k] = 0.;
            noisePhase[k] = 0.;
            noiseAmplitude[k] = 0.;
            beamPhase[k] = 0.;
            beamAmplitude[k] = 0.;
            Channel ca1 = ChannelFactory.defaultFactory().getChannel(myDoc.cav[k].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Wf_Dt");
            Channel ca2 = ChannelFactory.defaultFactory().getChannel(myDoc.cav[k].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "CtlRFPW");
            try {
                llrfDt = ca1.getValDbl();
                endS = ca2.getValDbl() - 2.0;
            } catch (ConnectionException ce) {
                myDoc.errormsg("Error connect " + ce);
            } catch (GetException ge) {
                myDoc.errormsg("Error read " + ge);
            }
            myDoc.startPt = (int) Math.round(endS / llrfDt);
            myDoc.pts = (int) Math.round(10.0 / llrfDt);
            amin[k] = 1.E8;
            amax[k] = -1.E8;
            pmin[k] = 1.E8;
            pmax[k] = -1.E8;
            noisecount[k] = 0;
            beamcount[k] = 0;
            totalphase[k] = 0.;
            for (int i = 0; i < correlated; i++) {
                beamIndx[i] = 0;
                ampPeak[i] = -1.E8;
                ampRecord[i] = (double[]) amp.get(k + i * myDoc.numberOfCav);
                phaseRecord[i] = (double[]) phs.get(k + i * myDoc.numberOfCav);
                for (int j = myDoc.startPt; j < myDoc.startPt + myDoc.pts; j++) {
                    if (ampRecord[i][j] > ampPeak[i]) ampPeak[i] = ampRecord[i][j];
                    if ((phaseRecord[i][j] - phaseRecord[correlated - 1][myDoc.startPt]) > 180) phaseRecord[i][j] = phaseRecord[i][j] - 360; else if ((phaseRecord[i][j] - phaseRecord[correlated - 1][myDoc.startPt]) < -180) phaseRecord[i][j] = phaseRecord[i][j] + 360;
                }
                amax[k] = Math.max(amax[k], ampPeak[i]);
            }
            for (int i = 0; i < correlated; i++) {
                if (ampPeak[i] > 0.85 * amax[k]) {
                    beamIndx[i] = 2;
                    beamcount[k]++;
                    for (int j = myDoc.startPt; j < myDoc.startPt + myDoc.pts; j++) totalphase[k] = totalphase[k] + phaseRecord[i][j];
                } else if (ampPeak[i] < noise2signal * amax[k]) {
                    beamIndx[i] = 1;
                    noisecount[k]++;
                    for (int j = myDoc.startPt; j < myDoc.startPt + myDoc.pts; j++) {
                        noisePhase[k] = noisePhase[k] + phaseRecord[i][j];
                        noiseAmplitude[k] = noiseAmplitude[k] + ampRecord[i][j];
                    }
                }
            }
            if (beamcount[k] > 0) totalphase[k] = totalphase[k] / (myDoc.pts * beamcount[k]);
            for (int i = 0; i < correlated; i++) {
                if (beamIndx[i] == 2) {
                    for (int j = myDoc.startPt; j < myDoc.startPt + myDoc.pts; j++) {
                        if (Math.abs(phaseRecord[i][j] - totalphase[k]) > 30) {
                            beamIndx[i] = 0;
                            beamcount[k]--;
                            break;
                        }
                    }
                }
            }
            for (int i = 0; i < correlated; i++) {
                if (beamIndx[i] == 2) {
                    signalAmplitude[k] = signalAmplitude[k] + ampPeak[i];
                    for (int j = myDoc.startPt; j < myDoc.startPt + myDoc.pts; j++) {
                        pmin[k] = Math.min(pmin[k], phaseRecord[i][j]);
                        pmax[k] = Math.max(pmax[k], phaseRecord[i][j]);
                        amin[k] = Math.min(amin[k], ampRecord[i][j]);
                        signalPhase[k] = signalPhase[k] + phaseRecord[i][j];
                    }
                }
            }
            if (beamcount[k] > 0) {
                signalPhase[k] = signalPhase[k] / (myDoc.pts * beamcount[k]);
                signalAmplitude[k] = signalAmplitude[k] / beamcount[k];
            }
            if (noisecount[k] > 0) {
                noisePhase[k] = noisePhase[k] / (noisecount[k] * myDoc.pts);
                noiseAmplitude[k] = noiseAmplitude[k] / (noisecount[k] * myDoc.pts);
            }
            Phasor signal = new Phasor(signalAmplitude[k], signalPhase[k] * Constant.rad);
            Phasor noise = new Phasor(noiseAmplitude[k], noisePhase[k] * Constant.rad);
            signal.minus(noise);
            beamAmplitude[k] = signal.getam();
            beamPhase[k] = Constant.deg * signal.getph();
            if (beamPhase[k] > 180) {
                beamPhase[k] = beamPhase[k] - 360;
            }
            myDoc.beamAmp[k] = beamAmplitude[k];
            myDoc.beamPhase[k] = beamPhase[k];
            myDoc.signalA[k] = signalAmplitude[k];
            myDoc.signalP[k] = signalPhase[k];
            myDoc.noiseA[k] = noiseAmplitude[k];
            myDoc.noiseP[k] = noisePhase[k];
        }
    }
}
