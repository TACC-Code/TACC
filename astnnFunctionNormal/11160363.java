class BackupThread extends Thread {
    public PositionsTable(MidiPlayer argPlayer) {
        player = argPlayer;
        sequencer = argPlayer.getSequencer();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(260, 300));
        final String[] names = { "#", Messages.getString("PositionsTable.Name") };
        dataModel = new AbstractTableModel() {

            private static final long serialVersionUID = 1L;

            public int getColumnCount() {
                return names.length;
            }

            public int getRowCount() {
                return positionsList.size();
            }

            public Object getValueAt(int row, int col) {
                if (col == 0) {
                    return new Integer(row + 1);
                } else {
                    return positionsList.get(row);
                }
            }

            public String getColumnName(int col) {
                return names[col];
            }

            public Class<? extends Object> getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }

            public boolean isCellEditable(int row, int col) {
                return col == 1;
            }

            public void setValueAt(Object aValue, int row, int col) {
                ((PositionData) getValueAt(row, col)).setName(aValue.toString());
            }
        };
        table = new JTable(dataModel);
        table.getColumn(names[1]).setCellEditor(new DefaultCellEditor(new JTextField()));
        TableColumn col = table.getColumn("#");
        col.setMaxWidth(20);
        table.sizeColumnsToFit(0);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (table.getSelectedRow() >= 0) {
                    PositionData posData = positionsList.get(table.getSelectedRow());
                    sequencer.setTickPosition(posData.getTickPosition());
                    sequencer.setTempoInBPM(posData.getSpeed());
                    try {
                        sequencer.setTrackDescriptions(posData.getChannelData());
                    } catch (MidiParserException e1) {
                        e1.printStackTrace();
                    } catch (InvalidMidiDataException e1) {
                        e1.printStackTrace();
                    }
                    player.setSeekSlider(posData.getTickPosition());
                    player.channelTableChanged();
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        EmptyBorder eb = new EmptyBorder(5, 5, 2, 5);
        scrollPane.setBorder(new CompoundBorder(eb, new EtchedBorder()));
        add(scrollPane, BorderLayout.CENTER);
        JPanel p1 = new JPanel(new GridBagLayout());
        addButton = new JButton(Messages.getString("AddButton"));
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PositionData newPositionData = new PositionData();
                newPositionData.setTickPosition(sequencer.getTickPosition());
                newPositionData.setSpeed(sequencer.getTempoInBPM());
                newPositionData.setName(Messages.getString("PositionsTable.NewPosition"));
                newPositionData.setChannelData(sequencer.getClonedTrackDescriptions());
                positionsList.add(newPositionData);
                tableChanged();
            }
        });
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        p1.add(addButton, gridBagConstraints);
        modifyButton = new JButton(Messages.getString("PositionsTable.ModifyButton"));
        modifyButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() >= 0) {
                    PositionData positionData = positionsList.get(table.getSelectedRow());
                    positionData.setTickPosition(sequencer.getTickPosition());
                    positionData.setSpeed(sequencer.getTempoInBPM());
                    positionData.setChannelData(sequencer.getClonedTrackDescriptions());
                    tableChanged();
                }
            }
        });
        gridBagConstraints.gridy = 1;
        p1.add(modifyButton, gridBagConstraints);
        removeButton = new JButton(Messages.getString("RemoveButton"));
        removeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = table.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    positionsList.remove(selectedRows[i]);
                }
                tableChanged();
            }
        });
        gridBagConstraints.gridy = 2;
        p1.add(removeButton, gridBagConstraints);
        saveButton = new JButton(Messages.getString("PositionsTable.SaveButton"));
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    FileOutputStream fos = new FileOutputStream(parentFile + ".sav");
                    ObjectOutputStream out = new ObjectOutputStream(fos);
                    out.writeObject(positionsList);
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        gridBagConstraints.gridy = 3;
        p1.add(saveButton, gridBagConstraints);
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        p1.add(new JPanel(), gridBagConstraints);
        add(p1, BorderLayout.EAST);
        add(new JLabel(Messages.getString("PositionsTable.Positions")), BorderLayout.NORTH);
        setFileName(null);
    }
}
