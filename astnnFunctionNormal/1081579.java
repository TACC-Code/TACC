class BackupThread extends Thread {
    static AudioProcess createSynthAudioProcess(AudioSynthesizer audosynth) throws MidiUnavailableException {
        AudioFormat.Encoding PCM_FLOAT = new AudioFormat.Encoding("PCM_FLOAT");
        AudioFormat format = new AudioFormat(PCM_FLOAT, 44100, 32, 2, 4 * 2, 44100, ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN));
        final AudioInputStream ais = audosynth.openStream(format, null);
        System.out.println("PCM_FLOAT Encoding used!");
        final AudioProcess synthVoice = new AudioProcess() {

            byte[] streamBuffer = null;

            float[] floatArray = null;

            FloatBuffer floatBuffer = null;

            public void close() {
            }

            public void open() {
            }

            public int processAudio(AudioBuffer buffer) {
                if (buffer == null) return 0;
                if (streamBuffer == null || streamBuffer.length != buffer.getSampleCount() * 8) {
                    ByteBuffer bytebuffer = ByteBuffer.allocate(buffer.getSampleCount() * 8).order(ByteOrder.nativeOrder());
                    streamBuffer = bytebuffer.array();
                    floatArray = new float[buffer.getSampleCount() * 2];
                    floatBuffer = bytebuffer.asFloatBuffer();
                }
                try {
                    ais.read(streamBuffer, 0, buffer.getSampleCount() * 8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                floatBuffer.position(0);
                floatBuffer.get(floatArray);
                float[] left = buffer.getChannel(0);
                float[] right = buffer.getChannel(1);
                for (int n = 0; n < buffer.getSampleCount() * 2; n += 2) {
                    left[n / 2] = floatArray[n];
                    right[n / 2] = floatArray[n + 1];
                }
                return AUDIO_OK;
            }
        };
        return synthVoice;
    }
}
