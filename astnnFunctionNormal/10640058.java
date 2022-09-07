class BackupThread extends Thread {
        public JukeControls() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JPanel p1 = new JPanel();
            p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
            p1.setBorder(new EmptyBorder(10, 0, 5, 0));
            JPanel p2 = new JPanel();
            startB = new JButton(Messages.getString("MidiPlayer.StartButton"));
            startB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        if (startB.getText().equals(Messages.getString("MidiPlayer.StartButton"))) {
                            startB.setText(Messages.getString("MidiPlayer.StopButton"));
                            playSound();
                            setComponentsEnabled(true);
                        } else {
                            stop();
                        }
                    } catch (InvalidMidiDataException e1) {
                        e1.printStackTrace();
                    } catch (MidiParserException e2) {
                        e2.printStackTrace();
                    }
                }
            });
            startB.setEnabled(false);
            p2.add(startB);
            pauseB = new JButton(Messages.getString("MidiPlayer.PauseButton"));
            pauseB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        if (pauseB.getText().equals(Messages.getString("MidiPlayer.PauseButton"))) {
                            sequencer.stop();
                            playbackMonitor.stop();
                            pauseB.setText(Messages.getString("MidiPlayer.ResumeButton"));
                        } else {
                            sequencer.startAt(sequencer.getTickPosition(), false);
                            speedSlider.setValue(sequencer.getTempoInBPM());
                            playbackMonitor.start();
                            pauseB.setText(Messages.getString("MidiPlayer.PauseButton"));
                        }
                    } catch (InvalidMidiDataException e1) {
                        e1.printStackTrace();
                    } catch (MidiParserException e2) {
                        e2.printStackTrace();
                    }
                }
            });
            pauseB.setEnabled(false);
            p2.add(pauseB);
            p1.add(p2);
            JPanel p3 = new JPanel();
            p1.add(p3);
            add(p1);
            JPanel p4 = new JPanel(new BorderLayout());
            JPanel p41 = new JPanel(new BorderLayout());
            EmptyBorder eb = new EmptyBorder(5, 5, 5, 5);
            BevelBorder bb = new BevelBorder(BevelBorder.LOWERED);
            p41.setBorder(new CompoundBorder(eb, bb));
            p41.add(playbackMonitor);
            seekSlider = new JSlider(JSlider.HORIZONTAL);
            seekSlider.setEnabled(false);
            seekSlider.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    sequencer.setTickPosition(getSliderTickPositionInSequence());
                    playbackMonitor.repaint();
                }
            });
            p41.add("South", seekSlider);
            p4.add("Center", p41);
            JPanel units = new JPanel();
            units.setLayout(new BoxLayout(units, BoxLayout.Y_AXIS));
            selectableUnit = new ButtonGroup();
            JRadioButton radioButton = new JRadioButton(Messages.getString("MidiPlayer.SecondsRel"));
            radioButton.getModel().setActionCommand("Seconds");
            radioButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setSeekSliderRange();
                    countInB.setEnabled(false);
                }
            });
            radioButton.setEnabled(false);
            units.add(radioButton);
            selectableUnit.add(radioButton);
            radioButton = new JRadioButton(Messages.getString("MidiPlayer.Seconds"));
            radioButton.getModel().setActionCommand("real Seconds");
            radioButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setSeekSliderRange();
                    countInB.setEnabled(false);
                }
            });
            radioButton.setEnabled(false);
            units.add(radioButton);
            selectableUnit.add(radioButton);
            radioButton = new JRadioButton(Messages.getString("MidiPlayer.Bars"));
            radioButton.getModel().setActionCommand("Bars");
            radioButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setSeekSliderRange();
                    countInB.setEnabled(true);
                }
            });
            units.add(radioButton);
            radioButton.setEnabled(false);
            selectableUnit.add(radioButton);
            selectableUnit.setSelected(radioButton.getModel(), true);
            countInB = new JCheckBox(Messages.getString("MidiPlayer.Count"));
            countInB.setEnabled(true);
            units.add(countInB);
            p4.add("East", units);
            add(p4);
            JPanel p5 = new JPanel();
            p5.setLayout(new GridBagLayout());
            p5.setBorder(new EmptyBorder(5, 5, 10, 5));
            gainSlider = new JSlider(0, GAIN_RANGE, GAIN_RANGE);
            gainSlider.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    int value = gainSlider.getValue();
                    TitledBorder border2 = (TitledBorder) gainSlider.getBorder();
                    String s = (border2).getTitle();
                    s = s.substring(0, s.indexOf('=') + 1) + String.valueOf(value);
                    try {
                        setGain();
                    } catch (MidiParserException e1) {
                        e1.printStackTrace();
                    } catch (InvalidMidiDataException e1) {
                        e1.printStackTrace();
                    }
                    border2.setTitle(s);
                    gainSlider.repaint();
                }
            });
            TitledBorder tb = new TitledBorder(new EtchedBorder());
            tb.setTitle(Messages.getString("MidiPlayer.Gain") + " = " + GAIN_RANGE);
            gainSlider.setBorder(tb);
            p5.add(gainSlider, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            speedSlider = new JSlider(10, 200, 80);
            tb = new TitledBorder(new EtchedBorder());
            tb.setTitle(Messages.getString("MidiPlayer.Speed") + " = 80");
            speedSlider.setBorder(tb);
            speedSlider.addChangeListener(speedChangeListener);
            speedSlider.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    int value = speedSlider.getValue();
                    TitledBorder border2 = (TitledBorder) speedSlider.getBorder();
                    String s = (border2).getTitle();
                    s = s.substring(0, s.indexOf('=') + 1) + value;
                    border2.setTitle(s);
                    speedSlider.repaint();
                }
            });
            p5.add(speedSlider, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            add(p5);
            channelTable = getChannelTable();
            add(new JScrollPane(channelTable));
        }
}
