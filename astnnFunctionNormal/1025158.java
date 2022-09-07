class BackupThread extends Thread {
    public StreamingMidiEventManager(Synthesizer synthesizer) {
        timerMap = new HashMap<Long, List<TimerEvent>>();
        isActive = true;
        try {
            synthesizer.open();
            channels = synthesizer.getChannels();
        } catch (MidiUnavailableException e) {
            throw new JFugueException(JFugueException.ERROR_PLAYING_MUSIC);
        }
        for (int channel = 0; channel < CHANNELS; channel++) {
            for (int layer = 0; layer < LAYERS; layer++) {
                time[channel][layer] = 0;
            }
            currentLayer[channel] = 0;
        }
        currentTrack = 0;
        initialTime = System.currentTimeMillis();
        currentTime = System.currentTimeMillis();
        Thread timerThread = new Thread(new Runnable() {

            public void run() {
                while (isActive) {
                    if (advanceTimers()) {
                        synchronized (timerMap) {
                            executeScheduledEvents();
                        }
                    }
                    pauseForTheCause();
                }
            }
        }, "StreamingMidiEventManager timer thread");
        timerThread.start();
    }
}
