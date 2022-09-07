class BackupThread extends Thread {
    private void play(Sound sound) {
        AudioInputStream sourceAIS = null;
        try {
            sourceAIS = AudioSystem.getAudioInputStream(sound.soundFile);
        } catch (UnsupportedAudioFileException e) {
            Debugger.print(Wisl.DEBUG_CRITICAL, "Sound file is not valid audio data recognized by " + "the system:\n " + sound.soundFile.getAbsolutePath());
            return;
        } catch (IOException e) {
            Debugger.print(Wisl.DEBUG_CRITICAL, "An I/O exception occurred in opening the file:\n" + sound.soundFile.getAbsolutePath());
            Debugger.print(Wisl.DEBUG_CRITICAL, e.toString());
            return;
        }
        AudioFormat sourceFormat = sourceAIS.getFormat();
        AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
        AudioInputStream decodedAIS = null;
        try {
            decodedAIS = AudioSystem.getAudioInputStream(targetFormat, sourceAIS);
        } catch (IllegalArgumentException iae) {
            Debugger.print(Wisl.DEBUG_CRITICAL, "The playback of the sound file is not supported:\n" + sound.soundFile.getAbsolutePath());
            closeAudioStreams(decodedAIS, sourceAIS);
            return;
        }
        SourceDataLine line = null;
        try {
            line = getLine(targetFormat);
        } catch (LineUnavailableException e) {
            Debugger.print(Wisl.DEBUG_CRITICAL, "Cannot open a SourceDataLine: \n" + e.getMessage());
            closeAudioStreams(decodedAIS, sourceAIS);
            return;
        }
        line.start();
        playloop: for (int repeat = 0; repeat < sound.repetitions; repeat++) {
            if (repeat != 0) {
                closeAudioStreams(decodedAIS, sourceAIS);
                try {
                    sourceAIS = AudioSystem.getAudioInputStream(sound.soundFile);
                } catch (UnsupportedAudioFileException e) {
                    Debugger.print(Wisl.DEBUG_CRITICAL, "Sound file is not valid audio data " + "recognized by the system:\n " + sound.soundFile.getAbsolutePath());
                    return;
                } catch (IOException e) {
                    Debugger.print(Wisl.DEBUG_CRITICAL, "An I/O exception occurred in opening the " + "file:\n" + sound.soundFile.getAbsolutePath());
                    Debugger.print(Wisl.DEBUG_CRITICAL, e.toString());
                    return;
                }
                decodedAIS = AudioSystem.getAudioInputStream(targetFormat, sourceAIS);
            }
            int nBytesRead = 0;
            while (nBytesRead != -1) {
                try {
                    nBytesRead = decodedAIS.read(audioBuffer, 0, audioBuffer.length);
                } catch (IOException e) {
                    Debugger.print(Wisl.DEBUG_CRITICAL, "An I/O exception occurred in reading the " + "file:\n" + sound.soundFile.getAbsolutePath());
                    Debugger.print(Wisl.DEBUG_CRITICAL, e.toString());
                    break playloop;
                }
                if (die) {
                    break playloop;
                }
                if (nBytesRead >= 0) {
                    if ((sound.volume != 100) && !SoundPlayer.isMuted) {
                        convertVolume(audioBuffer, sound.volume, targetFormat.isBigEndian());
                    }
                    if (SoundPlayer.isMuted) {
                        convertVolume(audioBuffer, 0, targetFormat.isBigEndian());
                    }
                    line.write(audioBuffer, 0, nBytesRead);
                }
            }
            line.drain();
        }
        closeAudioStreams(decodedAIS, sourceAIS);
        line.stop();
        line.close();
        die = false;
    }
}
