class BackupThread extends Thread {
    protected void initTables(ArrayList<BPM> bpms, ArrayList<MagnetMainSupply> quads, HashMap<MagnetMainSupply, Double> map) {
        allBPMs = bpms;
        qPSs = quads;
        designMap = map;
        tuneMeasurement = new TuneMeasurement[allBPMs.size()];
        xTune = new double[allBPMs.size()];
        yTune = new double[allBPMs.size()];
        xPhase = new double[allBPMs.size()];
        yPhase = new double[allBPMs.size()];
        posArray = new double[allBPMs.size()];
        this.setSize(960, 850);
        setLayout(edgeLayout);
        String[] bpmColumnNames = { "BPM", "XTune", "XPhase", "YTune", "YPhase", "Ignore" };
        bpmTableModel = new BpmTableModel(allBPMs, bpmColumnNames, this);
        String[] quadColumnNames = { "Quad PS", "Set Pt.", "Readback", "fitted Field", "new Set Pt." };
        quadTableModel = new QuadTableModel(qPSs, quadColumnNames);
        EdgeLayout edgeLayout1 = new EdgeLayout();
        bpmPane.setLayout(edgeLayout1);
        JLabel label = new JLabel("Select a BPM for Tune Measurement:");
        edgeLayout.setConstraints(label, 0, 0, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        bpmPane.add(label);
        bpmTable = new JTable(bpmTableModel);
        bpmTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        bpmTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel rowSM = bpmTable.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                } else {
                    int selectedRow = lsm.getMinSelectionIndex();
                    setSelectedBPM((allBPMs.get(selectedRow)).getId());
                    if (!badBPMs.contains(new Integer(selectedRow))) {
                        plotBPMData(selectedRow);
                    }
                }
            }
        });
        bpmChooserPane = new JScrollPane(bpmTable);
        bpmChooserPane.setPreferredSize(new Dimension(450, 300));
        bpmChooserPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        edgeLayout1.setConstraints(bpmChooserPane, 20, 0, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        bpmPane.add(bpmChooserPane);
        JPanel selection = new JPanel();
        selection.setLayout(new GridLayout(2, 2));
        selection.setPreferredSize(new Dimension(330, 60));
        String[] options = { "Get tune (fit)", "Get tune (FFT)" };
        selectBPM = new JComboBox(options);
        selectBPM.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.out.println("App mode is " + isOnline);
                quadTableModel.setAppMode(isOnline);
                if (((String) (((JComboBox) evt.getSource()).getSelectedItem())).equals("Get tune (fit)")) {
                    tuneByFit();
                } else if (((String) (((JComboBox) evt.getSource()).getSelectedItem())).equals("Get tune (FFT)")) {
                    tuneByFFT();
                }
                if (isOnline) {
                    snapshot = loggerSession.takeSnapshot();
                    snapshot1 = loggerSession1.takeSnapshot();
                    startTime = new Date();
                }
            }
        });
        selectBPM.setPreferredSize(new Dimension(60, 10));
        JButton config = new JButton("Config. FFT/fit");
        config.setActionCommand("configuration");
        config.setPreferredSize(new Dimension(80, 10));
        config.addActionListener(this);
        selection.add(config);
        JLabel dummy = new JLabel("");
        selection.add(dummy);
        selection.add(selectBPM);
        configDialog.setBounds(300, 300, 330, 300);
        configDialog.setTitle("Config. fit/FFT parameters...");
        numberFormat.setMaximumFractionDigits(6);
        dumpData.setActionCommand("dumpData");
        dumpData.addActionListener(this);
        dumpData.setEnabled(false);
        selection.add(dumpData);
        xTuneAvg = new JLabel("avg. x tune = ");
        edgeLayout1.setConstraints(xTuneAvg, 440, 0, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        bpmPane.add(xTuneAvg);
        yTuneAvg = new JLabel("avg. y tune = ");
        edgeLayout1.setConstraints(yTuneAvg, 460, 0, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        bpmPane.add(yTuneAvg);
        quadTable = new JTable(quadTableModel);
        quadTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        quadPane = new JScrollPane(quadTable);
        quadPane.setPreferredSize(new Dimension(450, 200));
        quadPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        edgeLayout1.setConstraints(quadPane, 550, 0, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        bpmPane.add(quadPane);
        quadCorrBtn = new JButton("Find Quad Error");
        quadCorrBtn.setActionCommand("findQuadError");
        quadCorrBtn.addActionListener(this);
        quadCorrBtn.setEnabled(false);
        edgeLayout.setConstraints(quadCorrBtn, 650, 500, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        add(quadCorrBtn);
        progBar = new JProgressBar();
        progBar.setMinimum(0);
        edgeLayout.setConstraints(progBar, 680, 500, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        add(progBar);
        setQuadBtn = new JButton("Set Quads");
        setQuadBtn.setActionCommand("setQuads");
        setQuadBtn.addActionListener(this);
        setQuadBtn.setEnabled(false);
        edgeLayout.setConstraints(setQuadBtn, 720, 500, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        add(setQuadBtn);
        JPanel paramConf = new JPanel();
        JLabel fitFunction = new JLabel("Fit function: A*exp(-c*x) * sin(2PI*(w*x + b)) + d");
        JPanel ampPane = new JPanel();
        ampPane.setLayout(new GridLayout(1, 2));
        JPanel maxTimePane = new JPanel();
        maxTimePane.setLayout(new GridLayout(1, 2));
        JLabel label6 = new JLabel("Max. no of iterations: ");
        df6 = new DecimalField(maxTime, 9, numberFormat);
        maxTimePane.add(label6);
        maxTimePane.add(df6);
        paramConf.add(maxTimePane);
        JPanel fitLengthPane = new JPanel();
        fitLengthPane.setLayout(new GridLayout(1, 2));
        JLabel label7 = new JLabel("fit up to turn number:");
        numberFormat.setMaximumFractionDigits(0);
        df7 = new DecimalField(len, 4, numberFormat);
        fitLengthPane.add(label7);
        fitLengthPane.add(df7);
        paramConf.add(fitLengthPane);
        JPanel fftPane = new JPanel();
        fftPane.setLayout(new GridLayout(1, 2));
        JLabel label8 = new JLabel("FFT array size: ");
        String[] fftChoice = { "16", "32", "64", "128", "256" };
        fftConf = new JComboBox(fftChoice);
        fftConf.setSelectedIndex(2);
        fftConf.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (((String) (((JComboBox) evt.getSource()).getSelectedItem())).equals("16")) {
                    fftSize = 16;
                } else if (((String) (((JComboBox) evt.getSource()).getSelectedItem())).equals("32")) {
                    fftSize = 32;
                } else if (((String) (((JComboBox) evt.getSource()).getSelectedItem())).equals("64")) {
                    fftSize = 64;
                } else if (((String) (((JComboBox) evt.getSource()).getSelectedItem())).equals("128")) {
                    fftSize = 128;
                } else if (((String) (((JComboBox) evt.getSource()).getSelectedItem())).equals("256")) {
                    fftSize = 256;
                }
            }
        });
        fftConf.setPreferredSize(new Dimension(30, 18));
        fftPane.add(label8);
        fftPane.add(fftConf);
        paramConf.add(fftPane);
        JPanel paramConfBtn = new JPanel();
        EdgeLayout edgeLayout3 = new EdgeLayout();
        paramConfBtn.setLayout(edgeLayout3);
        JButton done = new JButton("OK");
        done.setActionCommand("paramsSet");
        done.addActionListener(this);
        edgeLayout3.setConstraints(done, 0, 50, 0, 0, EdgeLayout.LEFT_BOTTOM, EdgeLayout.NO_GROWTH);
        paramConfBtn.add(done);
        JButton cancel = new JButton("Cancel");
        cancel.setActionCommand("cancelConf");
        cancel.addActionListener(this);
        edgeLayout3.setConstraints(cancel, 0, 170, 0, 0, EdgeLayout.LEFT_BOTTOM, EdgeLayout.NO_GROWTH);
        paramConfBtn.add(cancel);
        configDialog.getContentPane().setLayout(new BorderLayout());
        configDialog.getContentPane().add(fitFunction, BorderLayout.NORTH);
        configDialog.getContentPane().add(paramConf, BorderLayout.CENTER);
        configDialog.getContentPane().add(paramConfBtn, BorderLayout.SOUTH);
        edgeLayout1.setConstraints(selection, 350, 10, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        bpmPane.add(selection);
        edgeLayout.setConstraints(bpmPane, 10, 10, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        add(bpmPane);
        plotDisplayPane = new JTabbedPane();
        plotDisplayPane.setPreferredSize(new Dimension(430, 600));
        plotDisplayPane.addTab("Phase", phasePlotPane);
        plotDisplayPane.addTab("Pos", posPlotPane);
        plotDisplayPane.addTab("phase diff.", phaseDiffPlotPane);
        edgeLayout.setConstraints(plotDisplayPane, 0, 480, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        EdgeLayout el2 = new EdgeLayout();
        phasePlotPane.setLayout(el2);
        xPhasePlotPane = new BPMPlotPane(2);
        el2.setConstraints(xPhasePlotPane, 20, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phasePlotPane.add(xPhasePlotPane);
        yPhasePlotPane = new BPMPlotPane(3);
        el2.setConstraints(yPhasePlotPane, 245, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phasePlotPane.add(yPhasePlotPane);
        xBpmPlotPane = new BPMPlotPane(0);
        EdgeLayout el1 = new EdgeLayout();
        posPlotPane.setLayout(el1);
        el1.setConstraints(xBpmPlotPane, 20, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        posPlotPane.add(xBpmPlotPane);
        JPanel xTunePanel = new JPanel();
        xTunePanel.setLayout(new GridLayout(1, 2));
        JLabel xTuneLabel = new JLabel("X Tune:");
        numberFormat.setMaximumFractionDigits(6);
        dfXTune = new JTextField(15);
        dfXTune.setForeground(Color.RED);
        xTunePanel.add(xTuneLabel);
        xTunePanel.add(dfXTune);
        el1.setConstraints(xTunePanel, 245, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        posPlotPane.add(xTunePanel);
        yBpmPlotPane = new BPMPlotPane(1);
        el1.setConstraints(yBpmPlotPane, 275, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        posPlotPane.add(yBpmPlotPane);
        JPanel yTunePanel = new JPanel();
        yTunePanel.setLayout(new GridLayout(1, 2));
        JLabel yTuneLabel = new JLabel("Y Tune:");
        dfYTune = new JTextField(15);
        dfYTune.setForeground(Color.RED);
        yTunePanel.add(yTuneLabel);
        yTunePanel.add(dfYTune);
        el1.setConstraints(yTunePanel, 500, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        posPlotPane.add(yTunePanel);
        xPhDiffPlotPane = new BPMPlotPane(4);
        phaseDiffPlotPane.add(xPhDiffPlotPane);
        yPhDiffPlotPane = new BPMPlotPane(5);
        phaseDiffPlotPane.add(yPhDiffPlotPane);
        add(plotDisplayPane);
        for (int i = 0; i < allBPMs.size(); i++) {
            bpmTableModel.addRowName((allBPMs.get(i)).getId(), i);
            bpmTableModel.setValueAt("0", i, 1);
            bpmTableModel.setValueAt("0", i, 2);
            bpmTableModel.setValueAt("0", i, 3);
            bpmTableModel.setValueAt("0", i, 4);
            bpmTableModel.setValueAt(new Boolean(false), i, 5);
        }
        setPVChs = new Channel[qPSs.size()];
        rbPVChs = new Channel[qPSs.size()];
        setPVCell = new InputPVTableCell[qPSs.size()];
        rbPVCell = new InputPVTableCell[qPSs.size()];
        for (int i = 0; i < qPSs.size(); i++) {
            MagnetMainSupply mps = qPSs.get(i);
            setPVChs[i] = mps.getChannel(MagnetMainSupply.FIELD_SET_HANDLE);
            rbPVChs[i] = mps.getChannel(MagnetMainSupply.FIELD_RB_HANDLE);
            quadTableModel.addRowName(mps.getId(), i);
            quadTableModel.setValueAt("0", i, 3);
        }
    }
}
