class BackupThread extends Thread {
    public DssContext(SourceDataLine sourceDataLine, int sampleSize) {
        this.sourceDataLine = sourceDataLine;
        this.audioFormat = sourceDataLine.getFormat();
        this.sampleSize = sampleSize;
        channels = audioFormat.getChannels();
        frameSize = audioFormat.getFrameSize();
        ssib = audioFormat.getSampleSizeInBits();
        channelSize = frameSize / channels;
        audioSampleSize = (1 << (ssib - 1));
        this.channelsBuffer = new FloatBuffer[channels];
        for (int ch = 0; ch < channels; ch++) {
            channelsBuffer[ch] = FloatBuffer.allocate(sampleSize);
        }
    }
}
