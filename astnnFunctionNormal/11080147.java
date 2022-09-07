class BackupThread extends Thread {
    public static AudioFormat getWaveCompatibleAudioFormat(AudioFormat af) {
        return new AudioFormat((af.getSampleSizeInBits() == 8) ? AudioFormat.Encoding.PCM_UNSIGNED : AudioFormat.Encoding.PCM_SIGNED, af.getSampleRate(), af.getSampleSizeInBits(), af.getChannels(), af.getFrameSize(), af.getFrameRate(), false, af.properties());
    }
}
