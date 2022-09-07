class BackupThread extends Thread {
    private AudioInputStream toLittleEndian(AudioInputStream ais) {
        AudioFormat format = ais.getFormat();
        AudioFormat targetFormat = new AudioFormat(format.getEncoding(), format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), false);
        return AudioSystem.getAudioInputStream(targetFormat, ais);
    }
}
