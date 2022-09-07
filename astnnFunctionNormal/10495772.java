class BackupThread extends Thread {
    @Override
    public AudioFormat getFormat() {
        if (audioFormat == null) {
            decodeFrame();
            audioFormat = new AudioFormat(sampleBuffer.getSampleRate(), sampleBuffer.getBitsPerSample(), sampleBuffer.getChannels(), true, true);
            saved = sampleBuffer.getData();
        }
        return audioFormat;
    }
}
