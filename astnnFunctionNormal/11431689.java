class BackupThread extends Thread {
        @Override
        public void valueChanged(final ParameterChangeEvent<? extends Note> event) {
            final Note noteValue = noteParam.getValue();
            if (noteValue.intValue() >= 0) {
                try {
                    final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getMachine().getAttribute(getMachine().getDefinition().file).getValue());
                    final byte[] bytes = new byte[(int) audioInputStream.getFrameLength() * audioInputStream.getFormat().getFrameSize()];
                    audioInputStream.read(bytes);
                    final ShortBuffer shortbuffer = ByteBuffer.wrap(bytes).asShortBuffer();
                    buffer = new short[shortbuffer.capacity()];
                    for (int i = 0; i < buffer.length; i++) {
                        buffer[i] = shortbuffer.get();
                    }
                    stereo = audioInputStream.getFormat().getChannels() == 2;
                    ratio = refParam.getValue().getPeriodInSample() / noteValue.getPeriodInSample();
                    count = 0;
                } catch (final UnsupportedAudioFileException e) {
                    logError(e);
                } catch (final IOException e) {
                    logError(e);
                }
            } else {
                buffer = null;
            }
        }
}
