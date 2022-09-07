class BackupThread extends Thread {
    public void tap(FloatSampleBuffer buf, int delay, float weight) {
        for (int ch = 0; ch < buf.getChannelCount(); ch++) {
            tap(ch, buf, delay, weight);
        }
    }
}
