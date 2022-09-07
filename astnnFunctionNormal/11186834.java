class BackupThread extends Thread {
    private MidiPlayer() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            midiChannels = synthesizer.getChannels();
            Soundbank sb = synthesizer.getDefaultSoundbank();
            if (sb != null) {
                instruments = sb.getInstruments();
            }
            reservedChannels = new int[midiChannels.length];
        } catch (MidiUnavailableException mue) {
            throw new CadenzaGeneralException();
        }
    }
}
