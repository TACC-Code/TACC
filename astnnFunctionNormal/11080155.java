class BackupThread extends Thread {
    private static AudioInputStream convertPCMSampleSizeAndEndianess(int nSampleSizeInBits, AudioFormat.Encoding enc, boolean bBigEndian, AudioInputStream sourceStream) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat targetFormat = new AudioFormat(enc, sourceFormat.getSampleRate(), nSampleSizeInBits, sourceFormat.getChannels(), calculatePCMFrameSize(sourceFormat.getChannels(), nSampleSizeInBits), sourceFormat.getFrameRate(), bBigEndian, sourceFormat.properties());
        return AudioSystem.getAudioInputStream(targetFormat, sourceStream);
    }
}
