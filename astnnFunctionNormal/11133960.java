class BackupThread extends Thread {
    public void conform(FloatSampleBuffer buf) {
        while (getChannelCount() < buf.getChannelCount()) {
            addChannel(true);
        }
        if (getSampleRate() != buf.getSampleRate()) {
            setSampleRate(buf.getSampleRate());
            makeSilence();
        }
    }
}
