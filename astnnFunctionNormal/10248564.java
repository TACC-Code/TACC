class BackupThread extends Thread {
    protected int convert(byte[] inBuffer, byte[] outBuffer, int outByteOffset, int inFrameCount) {
        int sampleCount = inFrameCount * getOriginalStream().getFormat().getChannels();
        int byteCount = sampleCount * (getOriginalStream().getFormat().getSampleSizeInBits() / 8);
        if (m_floatBuffer == null) {
            m_floatBuffer = new FloatSampleBuffer();
        }
        m_floatBuffer.initFromByteArray(inBuffer, 0, byteCount, getOriginalStream().getFormat());
        convert(m_floatBuffer);
        m_floatBuffer.convertToByteArray(outBuffer, outByteOffset, intermediateFloatBufferFormat);
        return inFrameCount;
    }
}
