class BackupThread extends Thread {
    protected AudioFormat replaceNotSpecified(AudioFormat sourceFormat, AudioFormat targetFormat) {
        boolean bSetSampleSize = false;
        boolean bSetChannels = false;
        boolean bSetSampleRate = false;
        boolean bSetFrameRate = false;
        if (targetFormat.getSampleSizeInBits() == AudioSystem.NOT_SPECIFIED && sourceFormat.getSampleSizeInBits() != AudioSystem.NOT_SPECIFIED) {
            bSetSampleSize = true;
        }
        if (targetFormat.getChannels() == AudioSystem.NOT_SPECIFIED && sourceFormat.getChannels() != AudioSystem.NOT_SPECIFIED) {
            bSetChannels = true;
        }
        if (targetFormat.getSampleRate() == AudioSystem.NOT_SPECIFIED && sourceFormat.getSampleRate() != AudioSystem.NOT_SPECIFIED) {
            bSetSampleRate = true;
        }
        if (targetFormat.getFrameRate() == AudioSystem.NOT_SPECIFIED && sourceFormat.getFrameRate() != AudioSystem.NOT_SPECIFIED) {
            bSetFrameRate = true;
        }
        if (bSetSampleSize || bSetChannels || bSetSampleRate || bSetFrameRate || (targetFormat.getFrameSize() == AudioSystem.NOT_SPECIFIED && sourceFormat.getFrameSize() != AudioSystem.NOT_SPECIFIED)) {
            float sampleRate = bSetSampleRate ? sourceFormat.getSampleRate() : targetFormat.getSampleRate();
            float frameRate = bSetFrameRate ? sourceFormat.getFrameRate() : targetFormat.getFrameRate();
            int sampleSize = bSetSampleSize ? sourceFormat.getSampleSizeInBits() : targetFormat.getSampleSizeInBits();
            int channels = bSetChannels ? sourceFormat.getChannels() : targetFormat.getChannels();
            int frameSize = getFrameSize(targetFormat.getEncoding(), sampleRate, sampleSize, channels, frameRate, targetFormat.isBigEndian(), targetFormat.getFrameSize());
            targetFormat = new AudioFormat(targetFormat.getEncoding(), sampleRate, sampleSize, channels, frameSize, frameRate, targetFormat.isBigEndian());
        }
        return targetFormat;
    }
}
