class BackupThread extends Thread {
    @Override
    public void run() {
        try {
            AudioInputStream audioInputStream = openAudioInputStream();
            try {
                final SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(audioInputStream.getFormat());
                final AudioFormat audioFormat = sourceDataLine.getFormat();
                if (audioFormat.getChannels() > 2) {
                    System.err.println("music " + name + ": cannot handle more than two channels");
                    return;
                }
                if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                    System.err.println("music " + name + ": encoding must be PCM_SIGNED");
                    return;
                }
                if (audioFormat.getSampleSizeInBits() != 16) {
                    System.err.println("music " + name + ": sample size must be 16 bits");
                    return;
                }
                if (audioFormat.isBigEndian()) {
                    System.err.println("music " + name + ": cannot handle little endian encoding");
                    return;
                }
                sourceDataLine.open(audioInputStream.getFormat());
                try {
                    sourceDataLine.start();
                    try {
                        final byte[] buf = new byte[8192];
                        while (state < 3 && !Thread.currentThread().isInterrupted()) {
                            int len = audioInputStream.read(buf, 0, buf.length);
                            if (len == -1) {
                                final AudioInputStream newAudioInputStream = openAudioInputStream();
                                if (!newAudioInputStream.getFormat().matches(audioInputStream.getFormat())) {
                                    newAudioInputStream.close();
                                    System.err.println("music " + name + ": file format has changed");
                                    break;
                                }
                                final AudioInputStream oldAudioInputStream = audioInputStream;
                                audioInputStream = newAudioInputStream;
                                oldAudioInputStream.close();
                                len = audioInputStream.read(buf, 0, buf.length);
                                if (len == -1) {
                                    System.err.println("music " + name + ": cannot re-read file");
                                    break;
                                }
                            }
                            switch(state) {
                                case 0:
                                    for (int i = 0; i + 3 < len; i += 4) {
                                        volume *= VOLUME_STEP_PER_SAMPLE;
                                        if (volume >= 1F) {
                                            state = 1;
                                            volume = 1F;
                                            break;
                                        }
                                        convertSample(buf, i);
                                        convertSample(buf, i + 2);
                                    }
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    for (int i = 0; i + 3 < len; i += 4) {
                                        volume /= VOLUME_STEP_PER_SAMPLE;
                                        if (volume <= MIN_VALUE) {
                                            state = 3;
                                            len = i;
                                            break;
                                        }
                                        convertSample(buf, i);
                                        convertSample(buf, i + 2);
                                    }
                                    break;
                                default:
                                    throw new AssertionError();
                            }
                            sourceDataLine.write(buf, 0, len);
                        }
                        if (state != 4) {
                            sourceDataLine.drain();
                        }
                    } finally {
                        sourceDataLine.stop();
                    }
                } finally {
                    sourceDataLine.close();
                }
            } finally {
                audioInputStream.close();
            }
        } catch (final IOException ex) {
            System.err.println("music " + name + ": " + ex.getMessage());
        } catch (final LineUnavailableException ex) {
            System.err.println("music " + name + ": " + ex.getMessage());
        } catch (final UnsupportedAudioFileException ex) {
            System.err.println("music " + name + ": " + ex.getMessage());
        }
    }
}
