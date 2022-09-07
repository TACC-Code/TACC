class BackupThread extends Thread {
    @Override
    public void playOnce() {
        try {
            InputStream is = ResourceHandler.getInstance().getResourceAsStreamFromZip(filename);
            AudioInputStream ais = AudioSystem.getAudioInputStream(is);
            AudioFormat baseFormat = ais.getFormat();
            decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            audioInputStream = AudioSystem.getAudioInputStream(decodedFormat, ais);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(decodedFormat);
            if (line != null) {
                try {
                    line.open(decodedFormat);
                    byte[] data = new byte[4096];
                    line.start();
                    int nBytesRead;
                    while (!stop && (nBytesRead = audioInputStream.read(data, 0, data.length)) != -1) {
                        line.write(data, 0, nBytesRead);
                    }
                } catch (IOException e) {
                    stopPlaying();
                    System.out.println("WARNING - could not open \"" + filename + "\" - sound will be disabled");
                } catch (LineUnavailableException e) {
                    stopPlaying();
                    System.out.println("WARNING - audio device is unavailable to play \"" + filename + "\" - sound will be disabled");
                }
            } else stopPlaying();
        } catch (UnsupportedAudioFileException e) {
            finalize();
            System.out.println("WARNING - \"" + filename + "\" is a no supported MP3 file - sound will be disabled");
        } catch (IOException e) {
            finalize();
            System.out.println("WARNING - could not open \"" + filename + "\" - sound will be disabled");
        } catch (LineUnavailableException e) {
            finalize();
            System.out.println("WARNING - audio device is unavailable to play \"" + filename + "\" - sound will be disabled");
        }
    }
}
