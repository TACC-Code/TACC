class BackupThread extends Thread {
    public AudioFormat getFileFormat() {
        AudioFormat cbFormat = getCircBufFormat();
        AudioFormat prefFormat = getAudioSettings().getPreferredAudioFormat();
        if (AudioFormats.matches(cbFormat, prefFormat)) {
            return cbFormat;
        }
        if (AudioUtils.isPCM(prefFormat)) {
            return cbFormat;
        }
        if ((Math.abs(prefFormat.getSampleRate() - cbFormat.getSampleRate()) > 0.1) || (prefFormat.getChannels() != cbFormat.getChannels())) {
            return new AudioFormat(prefFormat.getEncoding(), cbFormat.getSampleRate(), prefFormat.getSampleSizeInBits(), cbFormat.getChannels(), AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED, false);
        }
        return prefFormat;
    }
}
