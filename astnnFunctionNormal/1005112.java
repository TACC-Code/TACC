class BackupThread extends Thread {
    public void play() {
        try {
            new Timer().schedule(new GetArt(), 100);
            new Timer().schedule(new GetWiki(), 200);
            MusicBoxView.playing = true;
            MusicBoxView.timeSkipped = 0;
            MusicBoxView.jLabel2.setText(currentTrack.getTrackLength());
            if (fFilename.toLowerCase().endsWith(".mp3")) {
                MP3File f = (MP3File) AudioFileIO.read(new File(fFilename));
                audioHeader = (MP3AudioHeader) f.getAudioHeader();
                numFrames = audioHeader.getNumberOfFrames();
                MusicBoxView.trackLength = audioHeader.getPreciseTrackLength();
            } else {
                if (!fFilename.toLowerCase().endsWith(".ape") && !fFilename.toLowerCase().endsWith(".au") && !fFilename.toLowerCase().endsWith(".aif")) {
                    AudioFile aF = AudioFileIO.read(new File(fFilename));
                    aH = aF.getAudioHeader();
                    MusicBoxView.trackLength = aH.getTrackLength();
                } else {
                    AudioFileFormat aff = AudioSystem.getAudioFileFormat(new File(fFilename));
                    Map map = aff.properties();
                    if (map.containsKey("duration")) {
                        MusicBoxView.trackLength = ((Long) map.get("duration")).doubleValue();
                    }
                }
            }
            if (fFilename.toLowerCase().endsWith(".flac")) {
                FlacAudioFileReader fafr = new FlacAudioFileReader();
                AudioInputStream flacAudioInputStream = fafr.getAudioInputStream(new File(fFilename));
                AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, false);
                Flac2PcmAudioInputStream f2pcmStream = new Flac2PcmAudioInputStream(flacAudioInputStream, format, 100);
                din = AudioSystem.getAudioInputStream(format, f2pcmStream);
                rawplay(format, din);
            } else if (fFilename.toLowerCase().endsWith(".ogg") || fFilename.toLowerCase().endsWith(".mp3") || fFilename.toLowerCase().endsWith(".wav") || fFilename.toLowerCase().endsWith(".aifc") || fFilename.toLowerCase().endsWith(".aiff") || fFilename.toLowerCase().endsWith(".aif") || fFilename.toLowerCase().endsWith(".au") || fFilename.toLowerCase().endsWith(".ape")) {
                try {
                    File file1 = new File(fFilename);
                    fileLength = file1.length();
                    in = null;
                    try {
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file1));
                        while (bis.available() < file1.length()) System.out.print("");
                        byte[] buf = new byte[bis.available()];
                        bis.read(buf, 0, buf.length);
                        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
                        in = AudioSystem.getAudioInputStream(bais);
                    } catch (Exception ex1) {
                        MusicBoxView.showErrorDialog(ex1);
                        FileInputStream f_in = new FileInputStream(fFilename);
                        Bitstream m = new Bitstream(f_in);
                        long start = m.header_pos();
                        fileLength = fileLength - start;
                        try {
                            m.close();
                        } catch (Exception ex) {
                            MusicBoxView.showErrorDialog(ex);
                        }
                        f_in = new FileInputStream(fFilename);
                        f_in.skip(start);
                        in = AudioSystem.getAudioInputStream(f_in);
                    }
                    din = null;
                    if (in != null) {
                        AudioFormat baseFormat = in.getFormat();
                        decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                        din = AudioSystem.getAudioInputStream(decodedFormat, in);
                        aFF = AudioSystem.getAudioFileFormat(file1);
                        rawplay(decodedFormat, din);
                        in.close();
                    }
                } catch (Exception ex1) {
                    MusicBoxView.showErrorDialog(ex1);
                    System.err.println("Cannot play file: " + fFilename);
                }
            }
            MusicBoxView.albumArtLabel.setText("Nothing is playing");
        } catch (Exception ex) {
            MusicBoxView.showFileUnavailable(currentTrack);
            mbv.stop();
            MusicBoxView.showErrorDialog(ex);
        }
        MusicBoxView.changed = 0;
        MusicBoxView.timeSkipped = 0;
    }
}
