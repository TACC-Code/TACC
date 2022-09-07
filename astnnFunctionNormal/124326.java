class BackupThread extends Thread {
    private static boolean isSameBitsChannelSampleRate(AudioFormat af1, AudioFormat af2) {
        return (af1.getSampleSizeInBits() == af2.getSampleSizeInBits()) && (af1.getChannels() == af2.getChannels()) && (af1.getSampleRate() == af2.getSampleRate());
    }
}
