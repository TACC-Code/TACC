class BackupThread extends Thread {
    public AudioFormat getSourceFormat() {
        if (m_network == null || isSourceCapture()) {
            AudioFormat prefFormat = getAudioSettings().getPreferredAudioFormat();
            if (AudioUtils.isPCM(prefFormat)) {
                return prefFormat;
            } else {
                return new AudioFormat(prefFormat.getSampleRate(), 16, prefFormat.getChannels(), true, false);
            }
        } else {
            return null;
        }
    }
}
