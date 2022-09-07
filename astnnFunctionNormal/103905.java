class BackupThread extends Thread {
    public void run() {
        AudioInputStream stream;
        AudioFormat format;
        SourceDataLine.Info info;
        SourceDataLine line;
        byte[] buffer;
        try {
            stream = AudioSystem.getAudioInputStream(new File(file));
            format = stream.getFormat();
            if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                stream = AudioSystem.getAudioInputStream(format, stream);
            }
            info = new DataLine.Info(SourceDataLine.class, stream.getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(stream.getFormat());
            line.start();
            int numRead = 0;
            buffer = new byte[line.getBufferSize()];
            while ((numRead = stream.read(buffer, 0, buffer.length)) >= 0 && !stop) {
                int offset = 0;
                while (offset < numRead && !stop) {
                    offset += line.write(buffer, offset, numRead - offset);
                }
            }
            line.drain();
            line.stop();
        } catch (IOException e) {
        } catch (LineUnavailableException e) {
        } catch (UnsupportedAudioFileException e) {
        } catch (NullPointerException e) {
        }
    }
}
