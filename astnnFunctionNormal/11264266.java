class BackupThread extends Thread {
    private boolean cavtuned() {
        int j = 0;
        trys++;
        double p1 = 0.;
        double p2 = 0.;
        double rev;
        double dt = 2.8;
        double endP = 1300.;
        try {
            for (int i = 0; i < myDoc.numberOfCav; i++) {
                Channel ca1 = cf.getChannel(myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Wf_Dt");
                Channel ca2 = cf.getChannel(myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "CtlRFPW");
                try {
                    dt = ca1.getValDbl();
                    endP = ca2.getValDbl();
                } catch (ConnectionException ce) {
                    myDoc.errormsg("Error connect " + ce);
                } catch (GetException ge) {
                    myDoc.errormsg("Error read " + ge);
                }
                endPt = (int) Math.round((endP + 100.) / dt);
                startPt = (int) Math.round(endP / dt);
                for (int k = 0; k < endPt - startPt; k++) {
                    if (phaseRecord[i][startPt + k] - phaseRecord[i][startPt] > 180) phaseRecord[i][startPt + k] -= 360; else if (phaseRecord[i][startPt + k] - phaseRecord[i][startPt] < -180) phaseRecord[i][startPt + k] += 360;
                    if (phaseRecord[i][endPt + k] - phaseRecord[i][startPt] > 180) phaseRecord[i][endPt + k] -= 360; else if (phaseRecord[i][endPt + k] - phaseRecord[i][startPt] < -180) phaseRecord[i][endPt + k] += 360;
                    p1 += phaseRecord[i][k + startPt];
                    p2 += phaseRecord[i][k + endPt];
                }
                if (Math.abs(p1 - p2) > 12.0) {
                    j++;
                    try {
                        Channel ca3 = cf.getChannel(myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "cavV");
                        if (ca3.getValDbl() > 2.5) {
                            myDoc.errormsg("Unsafe operation, tuning cavity with RF on!");
                            nottuned = false;
                            return false;
                        }
                        Channel ca4 = cf.getChannel("SCL_HPRF:Tun" + myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(12, 16) + "Mot");
                        rev = (p1 - p2) * myDoc.rate / myDoc.pts;
                        if (Math.abs(rev) > 15) {
                            myDoc.errormsg("Error, cavity detuned too far!");
                            return true;
                        } else if (trys % 2 == 1) ca4.putVal(ca1.getValDbl() - rev); else ca4.putVal(ca1.getValDbl() + rev);
                    } catch (ConnectionException ce) {
                        myDoc.errormsg("Error connection");
                        return true;
                    } catch (GetException ge) {
                        myDoc.errormsg("Error get PV value");
                        return true;
                    } catch (PutException pe) {
                        myDoc.errormsg("Error write to PV");
                        return true;
                    }
                }
            }
        } catch (NullPointerException ne) {
            return true;
        } catch (ArrayIndexOutOfBoundsException ae) {
            return true;
        }
        if (j > 0) return false; else return true;
    }
}
