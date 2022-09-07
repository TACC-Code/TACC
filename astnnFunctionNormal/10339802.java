class BackupThread extends Thread {
    public void open() {
        if ((!opening) && (!opened) && url != null) {
            opening = true;
            _fireAudioStateEvent(AudioStateEvent.AudioState.OPENING);
            Thread openThread = new Thread() {

                public void run() {
                    decoded_input_stream = null;
                    input_stream = null;
                    try {
                        input_stream = AudioSystem.getAudioInputStream(url);
                        input_stream.mark(256000000);
                    } catch (Exception e) {
                        input_stream = null;
                        e.printStackTrace();
                    }
                    if (input_stream != null) {
                        AudioFormat baseFormat = input_stream.getFormat();
                        decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                        decoded_input_stream = AudioSystem.getAudioInputStream(decodedFormat, input_stream);
                        DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
                        try {
                            source = (SourceDataLine) AudioSystem.getLine(info);
                            source.addLineListener(new LineListener() {

                                public void update(LineEvent evt) {
                                    LineEvent.Type eventType = evt.getType();
                                    if (eventType == LineEvent.Type.OPEN) {
                                        opened = true;
                                    }
                                    if (eventType == LineEvent.Type.CLOSE) {
                                        opened = false;
                                    }
                                    if (eventType == LineEvent.Type.START) {
                                        playing = true;
                                    }
                                    if (eventType == LineEvent.Type.STOP) {
                                        playing = false;
                                        _fireAudioStateEvent(AudioStateEvent.AudioState.STOPPED);
                                        if (duration <= source.getMicrosecondPosition() + 30000 + skipped_microseconds) {
                                            setPosition(0);
                                        }
                                    }
                                }
                            });
                            source.open();
                            gainControl = (FloatControl) source.getControl(FloatControl.Type.MASTER_GAIN);
                            gainControl.setValue(dB);
                            opened = true;
                            _fireAudioStateEvent(AudioStateEvent.AudioState.READY);
                        } catch (Exception e) {
                            _fireAudioStateEvent(AudioStateEvent.AudioState.OPEN_FAILED);
                            failed = true;
                        }
                    }
                    opening = false;
                }
            };
            openThread.start();
        }
    }
}
