class BackupThread extends Thread {
    protected static AudioFormat getFormatForPlaying(byte[] audioData) throws UnsupportedAudioFileException, IOException {
        AudioFormat format = AudioSystem.getAudioFileFormat(new ByteArrayInputStream(audioData)).getFormat();
        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true); else return format;
    }
}
