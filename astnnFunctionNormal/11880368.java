class BackupThread extends Thread {
        public int processAudio(AudioBuffer buf) {
            int n = buf.getSampleCount();
            float buff[] = buf.getChannel(0);
            for (int i = 0; i < n; i++, count++) {
                if (count % period == 0) buff[i] = 0.2f; else buff[i] = 0.0f;
            }
            return AUDIO_OK;
        }
}
