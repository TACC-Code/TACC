class BackupThread extends Thread {
    private static AudioInputStream convertSampleRate(float fSampleRate, AudioInputStream sourceStream) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat targetFormat = new AudioFormat(sourceFormat.getEncoding(), fSampleRate, sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), sourceFormat.getFrameSize(), fSampleRate, sourceFormat.isBigEndian(), sourceFormat.properties());
        return AudioSystem.getAudioInputStream(targetFormat, sourceStream);
    }
}
