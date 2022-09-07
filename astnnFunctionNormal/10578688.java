class BackupThread extends Thread {
    public void setupDSP(SourceDataLine line) {
        if (dsp != null) {
            int channels = line.getFormat().getChannels();
            if (channels == 1) {
                dsp.setChannelMode(KJDigitalSignalProcessingAudioDataConsumer.CHANNEL_MODE_MONO);
            } else {
                dsp.setChannelMode(KJDigitalSignalProcessingAudioDataConsumer.CHANNEL_MODE_STEREO);
            }
            int bits = line.getFormat().getSampleSizeInBits();
            if (bits == 8) {
                dsp.setSampleType(KJDigitalSignalProcessingAudioDataConsumer.SAMPLE_TYPE_EIGHT_BIT);
            } else {
                dsp.setSampleType(KJDigitalSignalProcessingAudioDataConsumer.SAMPLE_TYPE_SIXTEEN_BIT);
            }
        }
    }
}
