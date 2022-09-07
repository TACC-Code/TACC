class BackupThread extends Thread {
        public int processAudio(AudioBuffer buf) {
            int n = buf.getSampleCount();
            float buff[] = buf.getChannel(0);
            for (int i = 0; i < n; i++, count++) {
                if (latch-- < 0) {
                    if (buff[i] > threshold) {
                        latency = (int) (count % period);
                        latch = 100;
                    }
                    if (latch < -period) latency = NOSIGNAL;
                }
            }
            return AUDIO_OK;
        }
}
