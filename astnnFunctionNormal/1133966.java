class BackupThread extends Thread {
    protected byte[] convertBuffer(SoundBuffer buffer, SoundDescriptor soundDescriptor) {
        int channels = buffer.getNumberOfChannels();
        int length = getByteLength(buffer, soundDescriptor);
        byte[] outData = new byte[length];
        AudioFormat format = getAudioFormat(soundDescriptor);
        float ditherBits = 0;
        List<float[]> inDataList = new ArrayList<float[]>();
        try {
            for (int i = 0; i < channels; i++) {
                inDataList.add(buffer.getChannelData(i));
            }
        } catch (BadParameterException e) {
            e.printStackTrace();
        }
        int inOffset = 0;
        int outByteOffset = 0;
        FloatSampleTools.float2byte(inDataList, inOffset, outData, outByteOffset, (int) soundDescriptor.getNumberOfSamples(), format, ditherBits);
        return outData;
    }
}
