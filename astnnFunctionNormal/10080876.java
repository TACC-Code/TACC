class BackupThread extends Thread {
    public TerrainSynth(float lowest, float highest) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        this.lowest = lowest;
        this.highest = highest;
        calculateDifference();
        midiSynth = MidiSystem.getSynthesizer();
        midiSynth.open();
        midiChannels = midiSynth.getChannels();
        instruments = midiSynth.getDefaultSoundbank().getInstruments();
        for (int i = 0; i < instruments.length; i++) {
        }
        midiSynth.loadInstrument(instruments[185]);
    }
}
