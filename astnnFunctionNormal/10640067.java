class BackupThread extends Thread {
        private JTable getChannelTable() {
            final String[] names = { Messages.getString("MidiPlayer.Instrument"), Messages.getString("MidiPlayer.Active"), Messages.getString("MidiPlayer.Gain") };
            TableModel dataModel = new AbstractTableModel() {

                public int getColumnCount() {
                    return names.length;
                }

                public int getRowCount() {
                    return sequencer.getTrackDescriptions().size();
                }

                public Object getValueAt(int row, int col) {
                    Object result = null;
                    switch(col) {
                        case 0:
                            result = sequencer.getTrackDescriptions().get(row).name;
                            break;
                        case 1:
                            result = sequencer.getTrackDescriptions().get(row).active ? Boolean.TRUE : Boolean.FALSE;
                            break;
                        case 2:
                            result = sequencer.getTrackDescriptions().get(row).gain;
                            break;
                    }
                    return result;
                }

                public String getColumnName(int col) {
                    return names[col];
                }

                public Class<? extends Object> getColumnClass(int c) {
                    return getValueAt(0, c).getClass();
                }

                public boolean isCellEditable(int row, int col) {
                    return col == 0 ? false : true;
                }

                public void setValueAt(Object aValue, int row, int col) {
                    if (col == 1) {
                        sequencer.getTrackDescriptions().get(row).active = (Boolean) aValue;
                        try {
                            sequencer.notifySettingsChange();
                        } catch (MidiParserException e) {
                            e.printStackTrace();
                        } catch (InvalidMidiDataException e) {
                            e.printStackTrace();
                        }
                    } else if (col == 2) {
                        sequencer.getTrackDescriptions().get(row).gain = (Integer) aValue;
                        try {
                            sequencer.notifySettingsChange();
                        } catch (MidiParserException e) {
                            e.printStackTrace();
                        } catch (InvalidMidiDataException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            JTable result = new JTable(dataModel);
            result.getColumn(names[0]).setMinWidth(120);
            result.getColumn(names[2]).setCellEditor(new SliderEditor());
            result.getColumn(names[2]).setCellRenderer(new SliderRenderer());
            return result;
        }
}
