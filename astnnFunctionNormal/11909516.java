class BackupThread extends Thread {
    protected AudioInputStream decode(AudioInputStream stream) throws IOException {
        AudioFormat format = stream.getFormat();
        AudioFormat.Encoding encoding = format.getEncoding();
        AudioInputStream ret;
        if (encoding != AudioFormat.Encoding.PCM_SIGNED) {
            try {
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), false);
                ret = AudioSystem.getAudioInputStream(format, stream);
            } catch (Exception noConvert) {
                if (Debug.debug) {
                    Debug.getInstance().info(noConvert.getMessage());
                    Debug.getInstance().info("Trying 8 Bit sample-size conversion.");
                }
                format = new AudioFormat(format.getSampleRate(), 8, format.getChannels(), true, false);
                ret = AudioSystem.getAudioInputStream(format, stream);
            }
        } else {
            ret = stream;
        }
        return ret;
    }
}
