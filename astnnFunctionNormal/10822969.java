class BackupThread extends Thread {
    private void makeMainPanel() {
        JPanel mp = jpanels[MAIN_PANEL];
        mp.setLayout(new BorderLayout());
        graphPanel.setChooseModeButtonVisible(false);
        graphPanel.setHorLinesButtonVisible(false);
        graphPanel.setVerLinesButtonVisible(false);
        Font graphFont = new Font(getFont().getFamily(), Font.BOLD, 10);
        graphPanel.setAxisNameFontX(graphFont);
        graphPanel.setAxisNameFontY(graphFont);
        graphPanel.setNumberFont(graphFont);
        GridLimits gridLimits = graphPanel.getNewGridLimits();
        gridLimits.setNumberFormatX(new DecimalFormat("####.###"));
        gridLimits.setNumberFormatY(new DecimalFormat("####.###"));
        gridLimits.setLimitsAndTicksX(0., 100., 20., 5);
        gridLimits.setLimitsAndTicksY(0., 100., 20., 5);
        graphPanel.setExternalGL(gridLimits);
        graphPanel.getExternalGL().setYminOn(false);
        graphPanel.getExternalGL().setYmaxOn(false);
        graphPanel.getExternalGL().setXminOn(false);
        graphPanel.getExternalGL().setXmaxOn(false);
        graphPanel.setAxisNameX("Scanned PV");
        graphPanel.setAxisNameY("Measured PV");
        graphPanel.setOffScreenImageDrawing(true);
        rangePanel.addButtonPanel();
        rangePanel.registerIndependentValue(independValue);
        rangePanel.setCurrentValueRB(0.0);
        rangePanel.setCurrentValue(0.0);
        rangePanel.setLowLimit(0.0);
        rangePanel.setUppLimit(100.0);
        rangePanel.setStep(5.0);
        rangePanel.registerMeasurer(measuredVals);
        rangePanel.dimensionText.setText(" [a.u.]");
        measuredVals.addMeasuredValueInstance(measuredValue);
        valuatorValue.setLimitsManager(thresholdControl);
        valuatorMain.addExternalValuator(valuatorValue);
        measuredVals.setValuator(valuatorMain);
        measuredVals.setAvrgCntrl(avgControl);
        mSetPVsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                activatePanel(PV_SELECT_PANEL);
            }
        });
        mClearDataButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphPanel.removeAllGraphData();
                measuredValue.removeAllDataContainers();
            }
        });
        mReadDataButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                readDataFromDisk();
            }
        });
        mSaveDataButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveDataToDisk();
            }
        });
        mAnalysisButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateAnalysisGraphPanel();
                activatePanel(ANALISYS_PANEL);
            }
        });
        measuredVals.addNewSetOfMeasurementsListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateGraphPanel();
            }
        });
        mPV_ShowButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateGraphPanel();
            }
        });
        mPV_RB_ShowButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateGraphPanel();
            }
        });
        mScanPV_Name_Text.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                spScanPV_Text.setText(null);
                spScanPV_Text.setText(mScanPV_Name_Text.getText());
                Channel ch = null;
                if (mScanPV_Name_Text.getText().length() > 0) {
                    ch = ChannelFactory.defaultFactory().getChannel(mScanPV_Name_Text.getText());
                } else {
                    showInfoPanel("PV for scanning should be specified (X axis).", MAIN_PANEL);
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                try {
                    if (ch != null && !ch.writeAccess()) {
                        String msg = "PV for scanning:" + System.getProperty("line.separator");
                        msg = msg + "PV (" + ch.channelName() + ") does not have write access. Set the new PV name.";
                        Toolkit.getDefaultToolkit().beep();
                        showInfoPanel(msg, MAIN_PANEL);
                        return;
                    }
                    if (spScanPV_Text.getText().length() > 0) {
                        independValue.setChannelName(spScanPV_Text.getText());
                    } else {
                        independValue.setChannelName(null);
                    }
                } catch (ConnectionException expt) {
                    String msg = "Cannot connect to PVs:" + System.getProperty("line.separator");
                    msg = msg + mScanPV_Name_Text.getText() + System.getProperty("line.separator");
                    msg = msg + "Please, check EPICS or names of PVs.";
                    Toolkit.getDefaultToolkit().beep();
                    showInfoPanel(msg, MAIN_PANEL);
                }
            }
        });
        mScanPV_RB_Name_Text.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                spScanPV_RB_Text.setText(null);
                spScanPV_RB_Text.setText(mScanPV_RB_Name_Text.getText());
                Channel ch = null;
                if (mScanPV_RB_Name_Text.getText().length() > 0) {
                    ch = ChannelFactory.defaultFactory().getChannel(mScanPV_RB_Name_Text.getText());
                }
                try {
                    if (ch != null && !ch.readAccess()) {
                        String msg = "Read Back PV for scanning:" + System.getProperty("line.separator");
                        msg = msg + "PV (" + ch.channelName() + ") does not have read access. Set the new PV name.";
                        Toolkit.getDefaultToolkit().beep();
                        showInfoPanel(msg, MAIN_PANEL);
                        return;
                    }
                    if (spScanPV_RB_Text.getText().length() > 0) {
                        independValue.setChannelNameRB(spScanPV_RB_Text.getText());
                    } else {
                        independValue.setChannelNameRB(null);
                    }
                } catch (ConnectionException expt) {
                    String msg = "Cannot connect to PVs:" + System.getProperty("line.separator");
                    msg = msg + mScanPV_RB_Name_Text.getText() + System.getProperty("line.separator");
                    msg = msg + "Please, check EPICS or names of PVs.";
                    Toolkit.getDefaultToolkit().beep();
                    showInfoPanel(msg, MAIN_PANEL);
                }
            }
        });
        mMeasurePV_Name_Text.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                spMeasurePV_Text.setText(null);
                spMeasurePV_Text.setText(mMeasurePV_Name_Text.getText());
                Channel ch = null;
                if (mMeasurePV_Name_Text.getText().length() > 0) {
                    ch = ChannelFactory.defaultFactory().getChannel(mMeasurePV_Name_Text.getText());
                } else {
                    showInfoPanel("PV for measuring should be specified (Y axis).", MAIN_PANEL);
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                try {
                    if (ch != null && !ch.readAccess()) {
                        String msg = "PV for scanning:" + System.getProperty("line.separator");
                        msg = msg + "PV (" + ch.channelName() + ") does not have read access. Set the new PV name.";
                        Toolkit.getDefaultToolkit().beep();
                        showInfoPanel(msg, MAIN_PANEL);
                        return;
                    }
                    if (spMeasurePV_Text.getText().length() > 0) {
                        measuredValue.setChannelName(spMeasurePV_Text.getText());
                    } else {
                        measuredValue.setChannelName(null);
                    }
                } catch (ConnectionException expt) {
                    String msg = "Cannot connect to PVs:" + System.getProperty("line.separator");
                    msg = msg + mMeasurePV_Name_Text.getText() + System.getProperty("line.separator");
                    msg = msg + "Please, check EPICS or names of PVs.";
                    Toolkit.getDefaultToolkit().beep();
                    showInfoPanel(msg, MAIN_PANEL);
                }
            }
        });
        mValidationPV_Name_Text.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                spValidationPV_Text.setText(null);
                spValidationPV_Text.setText(mValidationPV_Name_Text.getText());
                Channel ch = null;
                if (mValidationPV_Name_Text.getText().length() > 0) {
                    ch = ChannelFactory.defaultFactory().getChannel(mValidationPV_Name_Text.getText());
                }
                try {
                    if (ch != null && !ch.readAccess()) {
                        String msg = "PV for scanning:" + System.getProperty("line.separator");
                        msg = msg + "PV (" + ch.channelName() + ") does not have read access. Set the new PV name.";
                        Toolkit.getDefaultToolkit().beep();
                        showInfoPanel(msg, MAIN_PANEL);
                        return;
                    }
                    if (spValidationPV_Text.getText().length() > 0) {
                        valuatorValue.setChannelName(spValidationPV_Text.getText());
                    } else {
                        valuatorValue.setChannelName(null);
                    }
                } catch (ConnectionException expt) {
                    String msg = "Cannot connect to PVs:" + System.getProperty("line.separator");
                    msg = msg + mValidationPV_Name_Text.getText() + System.getProperty("line.separator");
                    msg = msg + "Please, check EPICS or names of PVs.";
                    Toolkit.getDefaultToolkit().beep();
                    showInfoPanel(msg, MAIN_PANEL);
                }
            }
        });
        mReadPVsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updatePV_ValuesOnMainPanel();
            }
        });
        Font diplayControlPanelFont = new Font(getFont().getFamily(), Font.BOLD, 10);
        mClearDataButton.setFont(diplayControlPanelFont);
        mAnalysisButton.setFont(diplayControlPanelFont);
        mSaveDataButton.setFont(diplayControlPanelFont);
        mReadDataButton.setFont(diplayControlPanelFont);
        mPV_ShowButton.setFont(diplayControlPanelFont);
        mPV_RB_ShowButton.setFont(diplayControlPanelFont);
        mReadPVsButton.setFont(diplayControlPanelFont);
        mScannedPV_Label.setFont(diplayControlPanelFont);
        mScannedPV_RB_Label.setFont(diplayControlPanelFont);
        mMeasured_Label.setFont(diplayControlPanelFont);
        mValidationPV_Label.setFont(diplayControlPanelFont);
        mScannedPV_Val_Label.setFont(diplayControlPanelFont);
        mScannedPV_RB_Val_Label.setFont(diplayControlPanelFont);
        mMeasured_Val_Label.setFont(diplayControlPanelFont);
        mValidationPV_Val_Label.setFont(diplayControlPanelFont);
        mScanPV_Name_Text.setFont(diplayControlPanelFont);
        mScanPV_RB_Name_Text.setFont(diplayControlPanelFont);
        mMeasurePV_Name_Text.setFont(diplayControlPanelFont);
        mValidationPV_Name_Text.setFont(diplayControlPanelFont);
        EmptyBorder epmtyBorder = new EmptyBorder(0, 0, 0, 0);
        JPanel tmp_1 = new JPanel();
        tmp_1.setLayout(new BorderLayout());
        tmp_1.setBorder(BorderFactory.createTitledBorder(etchedBorder, "X-Y plot data"));
        tmp_1.setBackground(getBackground().darker());
        tmp_1.add(graphPanel, BorderLayout.CENTER);
        JPanel tmp_2 = new JPanel();
        tmp_2.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tmp_2.add(mClearDataButton);
        tmp_2.add(mReadDataButton);
        tmp_2.add(mSaveDataButton);
        tmp_2.add(mAnalysisButton);
        JPanel tmp_3 = new JPanel();
        tmp_3.setLayout(new BorderLayout());
        tmp_3.add(tmp_1, BorderLayout.CENTER);
        tmp_3.add(tmp_2, BorderLayout.SOUTH);
        JPanel tmp_4 = new JPanel();
        tmp_4.setLayout(new BorderLayout());
        tmp_4.add(mSetPVsButton, BorderLayout.NORTH);
        tmp_4.add(rangePanel, BorderLayout.CENTER);
        tmp_4.add(avgControl.getJPanel(), BorderLayout.SOUTH);
        JPanel tmp_5 = new JPanel();
        tmp_5.setLayout(new BorderLayout());
        tmp_5.add(tmp_4, BorderLayout.NORTH);
        tmp_5.add(thresholdControl.getJPanel(), BorderLayout.CENTER);
        JPanel tmp_6 = new JPanel();
        tmp_6.setLayout(new BorderLayout());
        tmp_6.setBorder(BorderFactory.createTitledBorder(etchedBorder, "Scan Control"));
        tmp_6.setBackground(getBackground().darker());
        tmp_6.add(tmp_5, BorderLayout.NORTH);
        JPanel tmp_7 = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        tmp_7.setLayout(gridbag);
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(1, 0, 1, 0);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.33;
        c.weighty = 1.0;
        gridbag.setConstraints(mScannedPV_Label, c);
        tmp_7.add(mScannedPV_Label);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.33;
        c.weighty = 1.0;
        gridbag.setConstraints(mScanPV_Name_Text, c);
        tmp_7.add(mScanPV_Name_Text);
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0.33;
        c.weighty = 1.0;
        gridbag.setConstraints(mScannedPV_Val_Label, c);
        tmp_7.add(mScannedPV_Val_Label);
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.33;
        c.weighty = 1.0;
        gridbag.setConstraints(mScannedPV_RB_Label, c);
        tmp_7.add(mScannedPV_RB_Label);
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0.33;
        c.weighty = 1.0;
        gridbag.setConstraints(mScanPV_RB_Name_Text, c);
        tmp_7.add(mScanPV_RB_Name_Text);
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 0.33;
        c.weighty = 1.0;
        gridbag.setConstraints(mScannedPV_RB_Val_Label, c);
        tmp_7.add(mScannedPV_RB_Val_Label);
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0.33;
        c.weighty = 1.0;
        gridbag.setConstraints(mMeasured_Label, c);
        tmp_7.add(mMeasured_Label);
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 0.33;
        c.weighty = 1.0;
        gridbag.setConstraints(mMeasurePV_Name_Text, c);
        tmp_7.add(mMeasurePV_Name_Text);
        c.gridx = 2;
        c.gridy = 3;
        c.weightx = 0.33;
        c.weighty = 1.0;
        gridbag.setConstraints(mMeasured_Val_Label, c);
        tmp_7.add(mMeasured_Val_Label);
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0.33;
        c.weighty = 1.0;
        gridbag.setConstraints(mValidationPV_Label, c);
        tmp_7.add(mValidationPV_Label);
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 0.33;
        c.weighty = 1.0;
        gridbag.setConstraints(mValidationPV_Name_Text, c);
        tmp_7.add(mValidationPV_Name_Text);
        c.gridx = 2;
        c.gridy = 4;
        c.weightx = 0.33;
        c.weighty = 1.0;
        gridbag.setConstraints(mValidationPV_Val_Label, c);
        tmp_7.add(mValidationPV_Val_Label);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 1.0;
        c.weighty = 1.0;
        gridbag.setConstraints(mReadPVsButton, c);
        tmp_7.add(mReadPVsButton);
        JPanel tmp_8 = new JPanel();
        tmp_8.setLayout(new GridLayout(0, 2));
        tmp_8.add(mPV_ShowButton);
        tmp_8.add(mPV_RB_ShowButton);
        JPanel tmp_9 = new JPanel();
        tmp_9.setLayout(new BorderLayout());
        tmp_9.setBorder(BorderFactory.createTitledBorder(etchedBorder, "Display Control"));
        tmp_9.setBackground(getBackground().darker());
        tmp_9.add(tmp_8, BorderLayout.CENTER);
        tmp_9.add(tmp_7, BorderLayout.SOUTH);
        JPanel tmp_10 = new JPanel();
        tmp_10.setLayout(new BorderLayout());
        tmp_10.add(tmp_6, BorderLayout.NORTH);
        tmp_10.add(tmp_9, BorderLayout.SOUTH);
        JPanel tmp_11 = new JPanel();
        tmp_11.setLayout(new BorderLayout());
        tmp_11.add(tmp_10, BorderLayout.NORTH);
        mp.add(tmp_11, BorderLayout.WEST);
        mp.add(tmp_3, BorderLayout.CENTER);
    }
}
