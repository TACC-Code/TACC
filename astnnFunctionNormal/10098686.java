class BackupThread extends Thread {
    private void initSequencer(Sequence seq) {
        try {
            sequencer = MidiSystem.getSequencer();
            if (sequencer == null) {
                throw new RuntimeException("Midi Sequencer unavailable");
            }
            if (!(sequencer instanceof Synthesizer)) {
                synthesizer = MidiSystem.getSynthesizer();
                synthesizer.open();
                Receiver synRx = synthesizer.getReceiver();
                Transmitter seqTx = sequencer.getTransmitter();
                seqTx.setReceiver(synRx);
                if (debug) {
                    new javax.swing.Timer(2000, new java.awt.event.ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            boolean needNL = false;
                            for (javax.sound.midi.MidiChannel c : synthesizer.getChannels()) {
                                if (c != null) {
                                    if (c.getMute()) {
                                        System.out.print(" m");
                                    } else {
                                        System.out.print(" " + ((c.getController(7) * 128) + c.getController(39)));
                                    }
                                    if (c.getSolo()) System.out.print("s");
                                    needNL = true;
                                }
                            }
                            if (needNL) System.out.println();
                            return;
                        }
                    }).start();
                }
            }
            sequencer.addMetaEventListener(new MetaEventListener() {

                public void meta(MetaMessage meta) {
                    if (meta.getType() == 47) {
                        sequencer.close();
                        if (synthesizer != null) {
                            synthesizer.close();
                        }
                    }
                }
            });
            sequencer.setSequence(seq);
        } catch (MidiUnavailableException e) {
            throw new RuntimeException("Midi default sequencer or synthesizer unavailable", e);
        } catch (InvalidMidiDataException e) {
            throw new IllegalArgumentException("Given data is not a MIDI sequence", e);
        }
    }
}
