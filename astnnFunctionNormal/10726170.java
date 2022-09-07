class BackupThread extends Thread {
    public void process(FloatSampleBuffer buffer) {
        for (int nChannel = 0; nChannel < buffer.getChannelCount(); nChannel++) {
            float[] afBuffer = buffer.getChannel(nChannel);
            for (int nSample = 0; nSample < buffer.getSampleCount(); nSample++) {
                afBuffer[nSample] *= m_fAmplitude;
            }
        }
    }
}
