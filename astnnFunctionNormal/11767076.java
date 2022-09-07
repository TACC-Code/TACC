class BackupThread extends Thread {
    public AudioFormat[] getTargetFormats(AudioFormat.Encoding targetEncoding, AudioFormat sourceFormat) {
        if (sourceFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) && targetEncoding instanceof FlacEncoding) {
            if (sourceFormat.getChannels() > 2 || sourceFormat.getChannels() <= 0 || sourceFormat.isBigEndian()) {
                AudioFormat[] formats = {};
                return formats;
            } else {
                AudioFormat[] formats = { new AudioFormat(targetEncoding, sourceFormat.getSampleRate(), -1, sourceFormat.getChannels(), -1, -1, false) };
                return formats;
            }
        } else if (sourceFormat.getEncoding() instanceof FlacEncoding && targetEncoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            AudioFormat[] formats = { new AudioFormat(sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), true, false) };
            return formats;
        } else {
            AudioFormat[] formats = {};
            return formats;
        }
    }
}
