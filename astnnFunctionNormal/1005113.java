class BackupThread extends Thread {
    public void play(SocketObject objectToPlay) {
        try {
            new Timer().schedule(new GetArt(), 100);
            new Timer().schedule(new GetWiki(), 200);
            MusicBoxView.playing = true;
            MusicBoxView.timeSkipped = 0;
            MusicBoxView.jLabel2.setText(currentTrack.getTrackLength());
            String time = currentTrack.getTrackLength();
            SimpleDateFormat format = new SimpleDateFormat("mm:ss");
            Date d = format.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int t = ((cal.get(Calendar.MINUTE) * 60) + cal.get(Calendar.SECOND));
            MusicBoxView.trackLength = (((double) t));
        } catch (ParseException ex) {
            mbv.stop();
            MusicBoxView.showErrorDialog(ex);
        }
        if (currentTrack.getFile().toLowerCase().endsWith(".flac")) {
            FlacAudioFileReader fafr = new FlacAudioFileReader();
            try {
                AudioInputStream flacAudioInputStream = fafr.getAudioInputStream(new ByteArrayInputStream(objectToPlay.getAudioBytes()));
                AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, false);
                Flac2PcmAudioInputStream f2pcmStream = new Flac2PcmAudioInputStream(flacAudioInputStream, format, 100);
                din = AudioSystem.getAudioInputStream(format, f2pcmStream);
                rawplay(format, din);
            } catch (UnsupportedAudioFileException ex) {
                MusicBoxView.showErrorDialog(ex);
            } catch (IOException ex) {
                MusicBoxView.showErrorDialog(ex);
            } catch (LineUnavailableException ex) {
                MusicBoxView.showErrorDialog(ex);
            }
        } else {
            byte[] buf = objectToPlay.getAudioBytes();
            fileLength = buf.length;
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            try {
                in = AudioSystem.getAudioInputStream(bais);
            } catch (Exception ex1) {
                MusicBoxView.showErrorDialog(ex1);
                Bitstream m = new Bitstream(bais);
                long start = m.header_pos();
                fileLength = fileLength - start;
                try {
                    m.close();
                } catch (Exception ex) {
                    MusicBoxView.showErrorDialog(ex);
                }
                bais = new ByteArrayInputStream(buf);
                bais.skip(start);
                try {
                    in = AudioSystem.getAudioInputStream(bais);
                } catch (UnsupportedAudioFileException ex) {
                    MusicBoxView.showErrorDialog(ex);
                } catch (IOException ex) {
                    MusicBoxView.showErrorDialog(ex);
                }
            }
            din = null;
            if (in != null) {
                try {
                    AudioFormat baseFormat = in.getFormat();
                    decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                    din = AudioSystem.getAudioInputStream(decodedFormat, in);
                    rawplay(decodedFormat, din);
                    in.close();
                } catch (IOException ex) {
                    MusicBoxView.showErrorDialog(ex);
                } catch (LineUnavailableException ex) {
                    MusicBoxView.showErrorDialog(ex);
                }
            }
        }
        MusicBoxView.changed = 0;
        MusicBoxView.timeSkipped = 0;
    }
}
