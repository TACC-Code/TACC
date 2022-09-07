class BackupThread extends Thread {
    public JTM_Mp3Player(File f, float gain) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        srcFile = f;
        in = AudioSystem.getAudioInputStream(f);
        baseFormat = in.getFormat();
        decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
        decodedStream = AudioSystem.getAudioInputStream(decodedFormat, in);
        data = new byte[4096];
        line = getLine(decodedFormat);
        setVolume(gain);
        setComplete(false);
    }
}
