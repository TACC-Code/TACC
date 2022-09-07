class BackupThread extends Thread {
    public static JizzAudioInputStream convertToMp3(JizzAudioSource src, int bitRate) throws UnsupportedAudioFileException, IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("Converting audio source " + src + " to mp3 with " + "destination bit rate " + bitRate);
        }
        JizzAudioInfo info = new JizzAudioInfo(src.getContributor(), src.getArtist(), src.getAlbum(), src.getTitle());
        AudioInputStream srcStream = src.getInputStream();
        AudioFormat srcFormat = srcStream.getFormat();
        if (logger.isTraceEnabled()) {
            logger.trace("Audio stream input format: " + srcFormat);
        }
        AudioFormat.Encoding destEncoding = Encodings.getEncoding("MPEG1L3");
        if (!AudioSystem.isConversionSupported(destEncoding, srcFormat)) {
            if (logger.isTraceEnabled()) {
                logger.trace("Direct conversion from " + srcFormat + " to " + destEncoding + " not possible - trying intermediate PCM format");
            }
            AudioFormat pcmFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, srcFormat.getSampleRate(), srcFormat.getSampleSizeInBits(), srcFormat.getChannels(), srcFormat.getFrameSize(), srcFormat.getFrameRate(), false);
            if (AudioSystem.isConversionSupported(pcmFormat, srcFormat)) {
                srcStream = AudioSystem.getAudioInputStream(pcmFormat, srcStream);
            } else {
                throw new UnsupportedAudioFileException("Cannot convert " + "source format " + srcFormat + " to destiantion format " + destEncoding);
            }
        }
        AudioInputStream destStream = AudioSystem.getAudioInputStream(destEncoding, srcStream);
        if (destStream == null) {
            throw new UnsupportedAudioFileException("Cannot convert " + "source format " + srcFormat + " to destination format " + destEncoding);
        }
        return new JizzAudioInputStream(info, destStream);
    }
}
