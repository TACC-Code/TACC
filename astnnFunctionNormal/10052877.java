class BackupThread extends Thread {
    @Test
    public void testOpenMidiDevice() throws Exception {
        FrinikaJVSTSynth synth = (FrinikaJVSTSynth) MidiSystem.getMidiDevice(new FrinikaJVSTSynthProvider.FrinikaJVSTSynthProviderInfo());
        final TargetDataLine line = (TargetDataLine) ((Mixer) synth).getLine(new Line.Info(TargetDataLine.class));
        AudioFormat.Encoding PCM_FLOAT = new AudioFormat.Encoding("PCM_FLOAT");
        AudioFormat format = new AudioFormat(PCM_FLOAT, 44100, 32, 2, 4 * 2, 44100, ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN));
        line.open(format);
        AudioInputStream ais = new AudioInputStream(line);
        assertTrue(AudioSystem.isConversionSupported(Encoding.PCM_SIGNED, ais.getFormat()));
        AudioInputStream convertedAis = AudioSystem.getAudioInputStream(Encoding.PCM_SIGNED, ais);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(convertedAis.getFormat());
        sdl.open();
        sdl.start();
        byte[] buf = new byte[16384];
        ShortMessage shm = new ShortMessage();
        shm.setMessage(ShortMessage.NOTE_ON, 1, 40, 127);
        synth.getReceiver().send(shm, -1);
        for (int n = 0; n < 20; n++) {
            int read = convertedAis.read(buf);
            sdl.write(buf, 0, read);
        }
    }
}
