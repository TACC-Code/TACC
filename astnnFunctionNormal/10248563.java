class BackupThread extends Thread {
    public FloatAudioInputStream(AudioInputStream sourceStream, AudioFormat targetFormat) {
        super(sourceStream, new AudioFormat(targetFormat.getEncoding(), sourceStream.getFormat().getSampleRate(), targetFormat.getSampleSizeInBits(), targetFormat.getChannels(), targetFormat.getChannels() * targetFormat.getSampleSizeInBits() / 8, sourceStream.getFormat().getFrameRate(), targetFormat.isBigEndian()));
        int floatChannels = targetFormat.getChannels();
        intermediateFloatBufferFormat = new AudioFormat(targetFormat.getEncoding(), sourceStream.getFormat().getSampleRate(), targetFormat.getSampleSizeInBits(), floatChannels, floatChannels * targetFormat.getSampleSizeInBits() / 8, sourceStream.getFormat().getFrameRate(), targetFormat.isBigEndian());
    }
}
