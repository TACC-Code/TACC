class BackupThread extends Thread {
    private void initPanel() {
        setLayout(new BorderLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        Box firstRowPanel = new Box(BoxLayout.LINE_AXIS);
        beginButton = new JButton();
        beginButton.setName("beginButton");
        beginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setLocationBegin();
            }
        });
        firstRowPanel.add(beginButton);
        rtButton = new JButton();
        rtButton.setName("rtButton");
        rtButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (rtButton.isSelected()) {
                    rbnbController.pause();
                } else {
                    rbnbController.monitor();
                }
            }
        });
        firstRowPanel.add(rtButton);
        playButton = new JButton();
        playButton.setName("playButton");
        playButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (playButton.isSelected()) {
                    rbnbController.pause();
                } else {
                    rbnbController.play();
                }
            }
        });
        firstRowPanel.add(playButton);
        endButton = new JButton();
        endButton.setName("endButton");
        endButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setLocationEnd();
            }
        });
        firstRowPanel.add(endButton);
        firstRowPanel.add(Box.createHorizontalStrut(8));
        SpinnerListModel playbackRateModel = new SpinnerListModel(playbackRates);
        playbackRateSpinner = new JSpinner(playbackRateModel);
        playbackRateSpinner.setName("playbackRateSpinner");
        JSpinner.ListEditor playbackRateEditor = new JSpinner.ListEditor(playbackRateSpinner);
        playbackRateEditor.getTextField().setEditable(true);
        playbackRateSpinner.setEditor(playbackRateEditor);
        playbackRateSpinner.setPreferredSize(new Dimension(80, playbackRateSpinner.getPreferredSize().height));
        playbackRateSpinner.setMinimumSize(playbackRateSpinner.getPreferredSize());
        playbackRateSpinner.setMaximumSize(playbackRateSpinner.getPreferredSize());
        playbackRateSpinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                playbackRateChanged();
            }
        });
        firstRowPanel.add(playbackRateSpinner);
        firstRowPanel.add(Box.createHorizontalStrut(8));
        timeScaleComboBox = new JComboBox();
        timeScaleComboBox.setName("timeScaleComboBox");
        timeScaleComboBox.setEditable(true);
        timeScaleComboBox.setPreferredSize(new Dimension(96, timeScaleComboBox.getPreferredSize().height));
        timeScaleComboBox.setMinimumSize(timeScaleComboBox.getPreferredSize());
        timeScaleComboBox.setMaximumSize(timeScaleComboBox.getPreferredSize());
        for (int i = 0; i < timeScales.length; i++) {
            timeScaleComboBox.addItem(DataViewer.formatSeconds(timeScales[i]));
        }
        timeScaleComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                timeScaleChange();
            }
        });
        firstRowPanel.add(timeScaleComboBox);
        firstRowPanel.add(Box.createHorizontalGlue());
        locationButton = new JButton();
        locationButton.setName("locationButton");
        locationButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                TimeRange timeRange = RBNBHelper.getChannelsTimeRange();
                double time = DateTimeDialog.showDialog(ControlPanel.this, rbnbController.getLocation(), timeRange.start, timeRange.end);
                if (time >= 0) {
                    rbnbController.setLocation(time);
                }
            }
        });
        firstRowPanel.add(locationButton);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new java.awt.Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.NORTHWEST;
        container.add(firstRowPanel, c);
        zoomTimeSlider = new TimeSlider();
        zoomTimeSlider.setRangeChangeable(false);
        zoomTimeSlider.addTimeAdjustmentListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new java.awt.Insets(0, 8, 0, 8);
        c.anchor = GridBagConstraints.NORTHWEST;
        container.add(zoomTimeSlider, c);
        JPanel zoomTimePanel = new JPanel();
        zoomTimePanel.setLayout(new BorderLayout());
        zoomMinimumLabel = new JLabel();
        zoomMinimumLabel.setName("zoomMinimumLabel");
        zoomTimePanel.add(zoomMinimumLabel, BorderLayout.WEST);
        zoomRangeLabel = new JLabel();
        zoomRangeLabel.setName("zoomRangeLabel");
        zoomRangeLabel.setHorizontalAlignment(JLabel.CENTER);
        zoomTimePanel.add(zoomRangeLabel, BorderLayout.CENTER);
        zoomMaximumLabel = new JLabel();
        zoomMaximumLabel.setName("zoomMaximumLabel");
        zoomTimePanel.add(zoomMaximumLabel, BorderLayout.EAST);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new java.awt.Insets(8, 8, 0, 8);
        c.anchor = GridBagConstraints.NORTHWEST;
        container.add(zoomTimePanel, c);
        globalTimeSlider = new TimeSlider();
        globalTimeSlider.setValueChangeable(false);
        globalTimeSlider.addTimeAdjustmentListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new java.awt.Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.NORTHWEST;
        container.add(globalTimeSlider, c);
        add(container, BorderLayout.CENTER);
        log.info("Initialized control panel.");
    }
}
