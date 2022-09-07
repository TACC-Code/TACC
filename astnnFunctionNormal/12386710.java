class BackupThread extends Thread {
    private void open() {
        try {
            sequencer = javax.sound.midi.MidiSystem.getSequencer();
            if (sequencer instanceof javax.sound.midi.Synthesizer) {
                synthesizer = (javax.sound.midi.Synthesizer) sequencer;
                channels = synthesizer.getChannels();
            }
            sequencer.open();
            sequencer.addMetaEventListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage() + "\nSound will not be available.");
        }
    }
}
