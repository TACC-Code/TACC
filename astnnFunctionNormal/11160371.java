class BackupThread extends Thread {
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
}
