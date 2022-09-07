class BackupThread extends Thread {
    public void setPosition(long microseconds) {
        if (!seeking) {
            seeking = true;
            current_seek_position = 0;
            _fireAudioStateEvent(AudioStateEvent.AudioState.SEEKING);
            try {
                skipped_microseconds = microseconds;
                source.close();
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
                    } catch (Exception e) {
                    }
                }
                input_stream.mark(256000000);
                source.open();
                EOF = false;
                int frame_size = decoded_input_stream.getFormat().getFrameSize();
                double frames_per_microsecond = (decoded_input_stream.getFormat().getFrameRate()) / 1000000.0;
                long frames_to_skip = (long) (frames_per_microsecond * microseconds);
                long bytes_to_skip = frames_to_skip * frame_size;
                byte[] data = new byte[8192];
                long nBytesRead = 0, nBytesWritten = 0;
                long bytes_skipped = 0;
                while (bytes_to_skip > 0 && nBytesRead != -1) {
                    try {
                        if (bytes_to_skip >= data.length) {
                            nBytesRead = decoded_input_stream.read(data, 0, data.length);
                            bytes_to_skip -= nBytesRead;
                            bytes_skipped += nBytesRead;
                            current_seek_position = (long) ((double) (bytes_skipped / frame_size) / frames_per_microsecond);
                            if (nBytesRead == -1) {
                                if (duration == 999999999) {
                                    long frames_skipped = bytes_skipped / frame_size;
                                    duration = (long) (frames_skipped / frames_per_microsecond);
                                    EOF = true;
                                    setPosition(0);
                                }
                            }
                        } else {
                            bytes_to_skip = 0;
                        }
                    } catch (Exception e) {
                        if (duration == 999999999) {
                            long frames_skipped = bytes_skipped / frame_size;
                            duration = (long) (frames_skipped / frames_per_microsecond);
                        }
                        e.printStackTrace();
                        nBytesRead = -1;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            seeking = false;
            if (EOF == true) {
                setPosition(0);
            }
            _fireAudioStateEvent(AudioStateEvent.AudioState.READY);
        }
    }
}
