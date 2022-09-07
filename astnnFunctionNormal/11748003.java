class BackupThread extends Thread {
    public boolean setFile(File file) {
        boolean bSuccess = true;
        m_file = file;
        if (Debug.getTraceAudio()) {
            Debug.out("PreListen.start(): begin");
        }
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(m_file);
        } catch (UnsupportedAudioFileException e) {
            if (Debug.getTraceAllExceptions()) {
                Debug.out(e);
            }
            bSuccess = false;
        } catch (IOException e) {
            if (Debug.getTraceAllExceptions()) {
                Debug.out(e);
            }
            bSuccess = false;
        }
        if (bSuccess) {
            AudioFormat format = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            boolean bIsSupportedDirectly = AudioSystem.isLineSupported(info);
            if (!bIsSupportedDirectly) {
                AudioFormat sourceFormat = format;
                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * (16 / 8), sourceFormat.getSampleRate(), false);
                if (Debug.getTraceAudio()) {
                    Debug.out("PreListen.start(): source format: " + sourceFormat);
                    Debug.out("PreListen.start(): target format: " + targetFormat);
                }
                audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
                format = audioInputStream.getFormat();
                if (Debug.getTraceAudio()) {
                    Debug.out("PreListen.start(): converted AIS: " + audioInputStream);
                }
                if (Debug.getTraceAudio()) {
                    Debug.out("PreListen.start(): converted format: " + format);
                }
                info = new DataLine.Info(Clip.class, format);
            }
            try {
                m_clip = (Clip) AudioSystem.getLine(info);
                m_clip.addLineListener(this);
                m_clip.open(audioInputStream);
            } catch (LineUnavailableException e) {
                if (Debug.getTraceAllExceptions()) {
                    Debug.out(e);
                }
                bSuccess = false;
            } catch (IllegalArgumentException e) {
                if (Debug.getTraceAllExceptions()) {
                    Debug.out(e);
                }
                bSuccess = false;
            } catch (IOException e) {
                if (Debug.getTraceAllExceptions()) {
                    Debug.out(e);
                }
                bSuccess = false;
            }
        }
        return bSuccess;
    }
}
