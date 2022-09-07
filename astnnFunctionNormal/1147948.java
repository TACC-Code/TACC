class BackupThread extends Thread {
    public SCLPhase(DriftBeamWindow window) {
        df = new DriftBeam(new SCLCavity("cav"));
        myWindow = window;
        iRow = 0;
        iCol = 0;
        deltaT = 1.E-6;
        myWindow.myDocument.bcmx = new double[1120];
        for (int i = 0; i < 1120; i++) {
            myWindow.myDocument.bcmx[i] = i;
        }
        bcmNamePv = "SCL_Diag:BCM00:currentTBT";
        btRun = new JButton("Run");
        btRun.setForeground(Color.RED);
        btRun.addActionListener(this);
        btReset = new JButton("Reset");
        btReset.addActionListener(this);
        btPulse = new JButton("Pulse_(us)");
        btPulse.addActionListener(this);
        btPulse.setEnabled(false);
        dlgPulse = new JDialog(window, "Beam Pulse Shape");
        dlgPulse.setPreferredSize(new Dimension(600, 400));
        EdgeLayout edgeLyt = new EdgeLayout();
        dlgPulse.getContentPane().setLayout(edgeLyt);
        dlgPulse.setLocationRelativeTo(myWindow);
        pulsePlot = new FunctionGraphsJPanel();
        pulsePlot.addMouseListener(new SimpleChartPopupMenu(pulsePlot));
        pulsePlot.setGraphBackGroundColor(Color.white);
        pulsePlot.setPreferredSize(new Dimension(500, 250));
        pulsePlot.setAxisNames("time (us)", "Beam current (A)");
        plotPulse.setColor(Color.RED);
        pulsePlot.addCurveData(plotPulse);
        pulsePlot.setLegendButtonVisible(false);
        edgeLyt.setConstraints(pulsePlot, 10, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        dlgPulse.add(pulsePlot);
        btBegin = new JButton("Update Beam Pulse");
        btBegin.addActionListener(this);
        edgeLyt.setConstraints(btBegin, 270, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        dlgPulse.add(btBegin);
        bcm = new JComboBox(bcms);
        bcm.addActionListener(this);
        edgeLyt.setConstraints(bcm, 270, 180, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        dlgPulse.add(bcm);
        tfShape = new DecimalField(iRow, 4);
        lbShape = new JLabel("No. of Sample Points:");
        edgeLyt.setConstraints(lbShape, 300, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        dlgPulse.add(lbShape);
        edgeLyt.setConstraints(tfShape, 300, 180, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        dlgPulse.add(tfShape);
        btEnd = new JButton("Use This Pulse");
        btEnd.addActionListener(this);
        edgeLyt.setConstraints(btEnd, 330, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        dlgPulse.add(btEnd);
        dlgPulse.pack();
        phaseWFPane = new JDialog(window, "Cavity LLRF Waveform");
        phaseWFPane.setPreferredSize(new Dimension(600, 400));
        EdgeLayout edgeLayout = new EdgeLayout();
        phaseWFPane.getContentPane().setLayout(edgeLayout);
        phaseWFPane.setLocationRelativeTo(myWindow);
        phasePlot = new FunctionGraphsJPanel();
        phasePlot.addMouseListener(new SimpleChartPopupMenu(phasePlot));
        phasePlot.setGraphBackGroundColor(Color.white);
        phasePlot.setPreferredSize(new Dimension(500, 250));
        phasePlot.setAxisNames("time (us)", "LLRF signal");
        plotPhase.setColor(Color.BLUE);
        plotAmplitude.setColor(Color.RED);
        phasePlot.addCurveData(plotPhase);
        phasePlot.addCurveData(plotAmplitude);
        edgeLayout.setConstraints(phasePlot, 10, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(phasePlot);
        JLabel phase = new JLabel("Phase average:");
        edgeLayout.setConstraints(phase, 270, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(phase);
        JLabel startLabel = new JLabel("begin (us): ");
        edgeLayout.setConstraints(startLabel, 290, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(startLabel);
        phaseStartF = new DecimalField(phaseStart, 6);
        edgeLayout.setConstraints(phaseStartF, 290, 90, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(phaseStartF);
        JLabel endLabel = new JLabel("end (us): ");
        edgeLayout.setConstraints(endLabel, 290, 190, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(endLabel);
        phaseEndF = new DecimalField(phaseEnd, 6);
        edgeLayout.setConstraints(phaseEndF, 290, 260, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(phaseEndF);
        lbTune = new JLabel("Motor Rev: ");
        edgeLayout.setConstraints(lbTune, 290, 360, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(lbTune);
        tfTune = new DecimalField(tuneRev, 6);
        edgeLayout.setConstraints(tfTune, 290, 440, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(tfTune);
        JButton updatePlot = new JButton("Update Plot");
        updatePlot.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!myWindow.myDocument.correlatorRunning) {
                    myWindow.myDocument.startCorrelator(e);
                }
                myWindow.myDocument.getCavSelector().getCavPhaseAvg(1298., 1308., 0);
                phasePlot.refreshGraphJPanel();
            }
        });
        edgeLayout.setConstraints(updatePlot, 320, 70, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(updatePlot);
        JButton phaseDone = new JButton("OK");
        phaseDone.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (myWindow.myDocument.getCavSelector().getSelectedRfCavity() != null) {
                    cav = myWindow.myDocument.getCavSelector().getSelectedRfCavity();
                    try {
                        double phaseAvg = myWindow.myDocument.getCavSelector().getCavPhaseAvg(numberFormat.parse(phaseStartF.getText()).doubleValue(), numberFormat.parse(phaseEndF.getText()).doubleValue(), 1);
                        phasePlot.refreshGraphJPanel();
                        setresults(phaseAvg);
                    } catch (java.text.ParseException pe) {
                        System.out.println("QLoaded is not in right number format!");
                    }
                } else {
                    System.out.println("No cavity selected!");
                }
                phaseWFPane.setVisible(false);
            }
        });
        edgeLayout.setConstraints(phaseDone, 320, 260, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(phaseDone);
        JButton phaseTune = new JButton("Tune Cavity");
        phaseTune.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                ChannelFactory caF = ChannelFactory.defaultFactory();
                cav = myWindow.myDocument.getCavSelector().getSelectedRfCavity();
                if (dCavFieldSP < 0.5) {
                    try {
                        Channel ca1 = caF.getChannel("SCL_HPRF:Tun" + cav.channelSuite().getChannel("cavAmpSet").getId().substring(12, 16) + "Mot");
                        tuneRev = tfTune.getValue();
                        ca1.putVal(ca1.getValDbl() + tuneRev);
                        Thread.sleep(2500);
                        Channel ca2 = caF.getChannel(cav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Wf_Dt");
                        pulseWidthdt = ca2.getValDbl();
                        myWindow.myDocument.getCavSelector().getCavPhaseAvg(1298., 1308., 0);
                    } catch (ConnectionException ce) {
                        System.out.println("Cannot connect to PV.");
                    } catch (GetException ge) {
                        System.out.println("Cannot get PV value.");
                    } catch (PutException pe) {
                        System.out.println("Cannot write to PV.");
                    } catch (InterruptedException ie) {
                        System.out.println("Cannot pause for tunning!");
                    }
                } else {
                    System.out.println("You may not detune this cavity, it's on!");
                }
            }
        });
        edgeLayout.setConstraints(phaseTune, 320, 380, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(phaseTune);
        phaseWFPane.pack();
        lbEnergy = new JLabel("Energy (MeV):");
        lbCurrent = new JLabel("Beam Current (mA):");
        lbQuality = new JLabel("Loaded Q:");
        lbDetuning = new JLabel("Res. Error (Hz):");
        lbAccFld = new JLabel("Eacc w/ TTF (MV/m):");
        lbPhaseSt = new JLabel("Cav Design Phase (deg):");
        lbPhaseBm = new JButton("Phase Avg Range");
        lbPhaseBm.setEnabled(false);
        lbPhaseBm.addActionListener(this);
        phaseAvgBtn = new JButton("20-pulse Avg.");
        phaseAvgBtn.setEnabled(false);
        phaseAvgBtn.addActionListener(this);
        phaseAvgBtn1 = new JButton("Cav Phase Avg.");
        phaseAvgBtn1.setEnabled(false);
        phaseAvgBtn1.addActionListener(this);
        lbPhaseCav = new JLabel("New Cav Phase Set Pt.:");
        lbDccFld = new JLabel("Beam Loading (MV/m):");
        lbRmsSize = new JLabel("RmsSize (deg):");
        lbOutput = new JLabel("Output Energy (MeV):");
        lbCavity = new JLabel("Cavity type:");
        lbBeamLoad = new JLabel("Measured Signal(WfA):");
        tfCurrent = new DecimalField(Bc, 6);
        tfEnergy = new DecimalField(Be, 6);
        tfPulse = new DecimalField(Pl, 6);
        tfQuality = new DecimalField(Ql, 6);
        tfDetuning = new DecimalField(Df, 6);
        tfAccFld = new DecimalField(Ac, 6);
        tfPhaseSt = new DecimalField(Cp, 6);
        tfPhaseBm = new DecimalField(Bp, 6);
        tfPhaseCav = new DecimalField(phaseCav, 12);
        tfDccFld = new DecimalField(dccFld, 12);
        tfRmsSize = new DecimalField(Br, 6);
        tfOutput = new DecimalField(outputE, 12);
        tfBeamLoad = new DecimalField(beamLoad, 12);
        tfCurrent.setValue(0.0);
        tfEnergy.setValue(0.0);
        tfPulse.setValue(0.0);
        tfQuality.setValue(0.0);
        tfDetuning.setValue(0.0);
        tfAccFld.setValue(0.0);
        tfPhaseSt.setValue(0.0);
        tfPhaseBm.setValue(0.0);
        tfPhaseCav.setValue(0.0);
        tfDccFld.setValue(0.0);
        tfRmsSize.setValue(0.0);
        tfOutput.setValue(0.0);
        tfBeamLoad.setValue(0.0);
        String[] cavTypes = { "High Beta", "Medium Beta" };
        cavType = new JComboBox(cavTypes);
        JPanel inputs = new JPanel();
        GridLayout gl = new GridLayout(5, 6);
        gl.setHgap(7);
        inputs.setLayout(gl);
        inputs.setBorder(BorderFactory.createTitledBorder("Inputs... "));
        inputs.add(lbEnergy);
        inputs.add(tfEnergy);
        inputs.add(lbCurrent);
        inputs.add(tfCurrent);
        inputs.add(btPulse);
        inputs.add(tfPulse);
        inputs.add(lbAccFld);
        inputs.add(tfAccFld);
        inputs.add(lbQuality);
        inputs.add(tfQuality);
        inputs.add(lbDetuning);
        inputs.add(tfDetuning);
        inputs.add(lbPhaseSt);
        inputs.add(tfPhaseSt);
        inputs.add(lbRmsSize);
        inputs.add(tfRmsSize);
        inputs.add(phaseAvgBtn1);
        inputs.add(tfPhaseBm);
        inputs.add(lbCavity);
        inputs.add(cavType);
        cavFieldSP = new JLabel("Field Set Pt.:");
        inputs.add(cavFieldSP);
        cavOff.setEnabled(false);
        cavOff.addActionListener(this);
        inputs.add(cavOff);
        inputs.add(phaseAvgBtn);
        inputs.add(lbPhaseBm);
        JLabel dummy2 = new JLabel("");
        JLabel dummy3 = new JLabel("");
        JLabel dummy4 = new JLabel("");
        inputs.add(cavName);
        inputs.add(dummy2);
        inputs.add(dummy3);
        inputs.add(dummy4);
        inputs.add(btReset);
        inputs.add(btRun);
        JPanel results = new JPanel();
        results.setBorder(BorderFactory.createTitledBorder("Results... "));
        GridLayout gl1 = new GridLayout(2, 6);
        gl1.setHgap(5);
        results.setLayout(gl1);
        cavPhaseSP = new JLabel("Phase Set Pt.:");
        results.add(lbPhaseCav);
        results.add(tfPhaseCav);
        results.add(lbDccFld);
        results.add(tfDccFld);
        results.add(lbBeamLoad);
        results.add(tfBeamLoad);
        results.add(cavPhaseSP);
        setPhase.setForeground(Color.RED);
        setPhase.setEnabled(false);
        setPhase.addActionListener(this);
        results.add(setPhase);
        JLabel dummy5 = new JLabel("");
        JLabel dummy6 = new JLabel("");
        results.add(lbOutput);
        results.add(tfOutput);
        results.add(dummy5);
        results.add(dummy6);
        add(inputs);
        add(results);
        setBounds(100, 100, 600, 300);
        setVisible(true);
    }
}
