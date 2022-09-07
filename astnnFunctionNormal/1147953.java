class BackupThread extends Thread {
    public void actionPerformed(ActionEvent ae) {
        ChannelFactory cf = ChannelFactory.defaultFactory();
        if (ae.getActionCommand().equals("Reset")) {
            reset(ae);
        } else if (ae.getActionCommand().equals("Pulse_(us)")) {
            dlgPulse.setVisible(true);
        } else if (ae.getActionCommand().equals("Phase Avg Range")) {
            phaseWFPane.setVisible(true);
            if (!myWindow.myDocument.correlatorRunning) {
                myWindow.myDocument.startCorrelator(ae);
            }
        } else if (ae.getActionCommand().equals("20-pulse Avg.")) {
            try {
                double phaseAvg = myWindow.myDocument.getCavSelector().getCavPhaseAvg(numberFormat.parse(phaseStartF.getText()).doubleValue(), numberFormat.parse(phaseEndF.getText()).doubleValue(), 20);
                noiseA = myWindow.myDocument.getCavSelector().getNoiseA();
                noiseP = myWindow.myDocument.getCavSelector().getNoiseP();
                ampFlu = myWindow.myDocument.getCavSelector().getAmpFlu();
                phaseFlu = myWindow.myDocument.getCavSelector().getPhaseFlu();
                if (ampFlu > 5.) {
                    tfBeamLoad.setForeground(Color.RED);
                } else if (ampFlu > 2.5) {
                    tfBeamLoad.setForeground(Color.BLUE);
                } else {
                    tfBeamLoad.setForeground(Color.BLACK);
                }
                if (phaseFlu > 5.) {
                    tfPhaseBm.setForeground(Color.RED);
                } else if (phaseFlu > 2.5) {
                    tfPhaseBm.setForeground(Color.BLUE);
                } else {
                    tfPhaseBm.setForeground(Color.BLACK);
                }
                setresults(phaseAvg);
            } catch (java.text.ParseException pe) {
                System.out.println("Error in get phase");
            }
        } else if (ae.getActionCommand().equals("Cav Phase Avg.")) {
            if (!myWindow.myDocument.correlatorRunning) {
                myWindow.myDocument.startCorrelator(ae);
            }
            try {
                double phaseAvg = myWindow.myDocument.getCavSelector().getCavPhaseAvg(numberFormat.parse(phaseStartF.getText()).doubleValue(), numberFormat.parse(phaseEndF.getText()).doubleValue(), 1);
                phasePlot.refreshGraphJPanel();
                setresults(phaseAvg);
            } catch (java.text.ParseException pe) {
                System.out.println("Error in get phase");
            }
        } else if (ae.getActionCommand().equals("Update Beam Pulse")) {
            try {
                Channel ca1 = cf.getChannel(bcmNamePv);
                bArry = ca1.getArrDbl();
                setPulsePlot(bArry, myWindow.myDocument.bcmx);
                pulsePlot.refreshGraphJPanel();
                myWindow.myDocument.beamShape = getITBTWithBeamOnly(bArry);
                iRow = myWindow.myDocument.beamShape.length;
                tfShape.setValue(iRow);
                tfPulse.setValue(iRow * deltaT * 1.E6);
                iCol = 0;
            } catch (NullPointerException ne) {
                System.out.println("Check PV name " + bcmNamePv);
            } catch (ConnectionException ce) {
                System.out.println("Cannot connect " + bcmNamePv);
            } catch (GetException ge) {
                System.out.println("Cannot value(s) " + bcmNamePv);
            } catch (NoSuchChannelException nse) {
                System.out.println("No channel " + bcmNamePv);
            }
        } else if (ae.getActionCommand().equals(bcm.getActionCommand())) {
            currentBCM = bcm.getSelectedIndex();
            switch(currentBCM) {
                case 0:
                    bcmDtPv = "SCL_Diag:BCM00b:SamplePeriod";
                    bcmNamePv = "SCL_Diag:BCM00:currentTBT";
                    break;
                case 5:
                    bcmDtPv = "DTL_Diag:BCM400:SamplePeriod";
                    bcmNamePv = "DTL_Diag:BCM400:currentTBT";
                    break;
                case 6:
                    bcmDtPv = "CCL_Diag:BCM102:SamplePeriod";
                    bcmNamePv = "CCL_Diag:BCM102:currentTBT";
                    break;
                case 3:
                    bcmDtPv = "HEBT_Diag:BCM01:SamplePeriod";
                    bcmNamePv = "HEBT_Diag:BCM01:currentTBT";
                    break;
                case 2:
                    bcmDtPv = "DTL_Diag:BCM02t248:tSamplePeriod";
                    bcmNamePv = "MEBT_Diag:BCM11:currentTBT";
                    break;
                case 4:
                    bcmDtPv = "DTL_Diag:BCM02t248:tSamplePeriod";
                    bcmNamePv = "DTL_Diag:BCM200:currentTBT";
                    break;
                default:
                    bcmDtPv = "DTL_Diag:BCM02t248:tSamplePeriod";
                    bcmNamePv = "MEBT_Diag:BCM02:currentTBT";
            }
        } else if (ae.getActionCommand().equals("Use This Pulse")) {
            if (iRow > 1) {
                iCol = 1;
            } else {
                iCol = 0;
            }
            dlgPulse.setVisible(false);
        } else if (ae.getActionCommand().equals("Run")) {
            if (tfDccFld.getValue() != 0. && cav != null) {
                setCavity(cav);
            }
            df.getCavity().buildcavity();
            Bc = tfCurrent.getValue();
            Be = tfEnergy.getValue();
            if (Be > 90.0) {
                if (iCol == 1) {
                    df.setshape(iRow);
                    for (int p = 0; p < iRow; p++) df.setpulse(p, deltaT * p, myWindow.myDocument.beamShape[p]);
                    Pl = deltaT * iRow;
                    df.setbeam(1.0, Pl, Be);
                } else {
                    df.setshape(1);
                    if (iRow > 1) {
                        Pl = deltaT * iRow;
                        df.setbeam(0.001 * Bc, Pl, Be);
                    } else {
                        Pl = tfPulse.getValue();
                        df.setbeam(0.001 * Bc, 0.000001 * Pl, Be);
                    }
                }
                NumberFormat nf = NumberFormat.getNumberInstance();
                try {
                    Ql = nf.parse(tfQuality.getText()).doubleValue();
                } catch (java.text.ParseException pe) {
                    System.out.println("QLoaded is not in right number format!");
                }
                Df = tfDetuning.getValue();
                Ac = tfAccFld.getValue();
                if (Ql < 60.0) Ql = 7.2E5;
                df.setcavity(Ql, Df, Ac, freq);
                Cp = tfPhaseSt.getValue();
                Bp = tfPhaseBm.getValue();
                Br = tfRmsSize.getValue();
                if (Br < 0.3) Br = 2.5;
                df.setphase(Cp, Bp, Br);
                numberFormat.setMaximumFractionDigits(4);
                phaseCav = df.findphase();
                tfPhaseCav.setText(numberFormat.format(phaseCav));
                dccFld = df.getloading();
                if (tfDccFld.getValue() < 0.09) {
                    tfPhaseCav.setForeground(Color.RED);
                } else if (tfDccFld.getValue() < 0.4) {
                    tfPhaseCav.setForeground(Color.BLUE);
                } else {
                    tfPhaseCav.setForeground(Color.BLACK);
                }
                tfDccFld.setText(numberFormat.format(dccFld));
                outputE = df.getBeam().getenergy();
                tfOutput.setText(numberFormat.format(outputE));
                if (Math.abs(phaseCav - dCavPhaseSP) > 20.) {
                    cavPhaseSP.setForeground(Color.RED);
                } else if (Math.abs(phaseCav - dCavPhaseSP) > 5.) {
                    cavPhaseSP.setForeground(Color.BLUE);
                } else {
                    cavPhaseSP.setForeground(Color.BLACK);
                }
            }
            if (myWindow.myDocument.isOnline) setPhase.setEnabled(true); else setPhase.setEnabled(false);
        } else if (ae.getActionCommand().equals("Set Cav. Phase")) {
            try {
                cav.setCavPhase(phaseCav);
                if (dCavFieldSP < 5.) {
                    Channel ca2 = cf.getChannel(cav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "RunState");
                    ca2.putVal("Ramp");
                }
                cavPhaseSP.setText("Phase Set Pt.: " + cav.getCavPhaseSetPoint());
            } catch (ConnectionException ce) {
                System.out.println("Cannot connect to PV!");
            } catch (PutException pe) {
                System.out.println("Cannot write to PV!");
            } catch (GetException ge) {
                System.out.println("Cannot get phase set PV!");
            }
        } else if (ae.getActionCommand().equals("Turn off Cav.")) {
            try {
                Channel ca1 = cf.getChannel(cav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "LoopOff");
                if (dCavFieldSP > 3.) {
                    Channel LoopOff = cf.getChannel(cav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "LoopOff");
                    double Fld = dCavFieldSP;
                    LoopOff.putVal("Close!");
                    int steps = (int) Math.round((Fld - 2.5) / 2.);
                    for (int i = 1; i < steps; i++) {
                        cav.setCavAmp(Fld - 2 * i);
                        Thread.sleep(500);
                    }
                    cav.setCavAmp(2.5);
                    Thread.sleep(3000);
                    dCavFieldSP = 2.5;
                }
                if (dCavFieldSP < 3.) {
                    Channel RFKill = cf.getChannel(cav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "RFKill");
                    RFKill.putVal("Kill");
                    dCavFieldSP = 0.0;
                }
            } catch (ConnectionException ce) {
                System.out.println("Cannot connect to PV.");
            } catch (PutException pe) {
                System.out.println("Cannot write to PV.");
            } catch (InterruptedException ie) {
                System.out.println("Cannot pause for tunning!");
            }
        }
    }
}
