class BackupThread extends Thread {
    public AudioFormat getTargetFormat(AudioFormat sourceFormat) {
        return new AudioFormat(MpegEncoding.MPEG1L3, sourceFormat.getSampleRate(), AudioSystem.NOT_SPECIFIED, sourceFormat.getChannels(), AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED, false);
    }
}
