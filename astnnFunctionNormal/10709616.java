class BackupThread extends Thread {
    protected void initialize() {
        starttime = 0;
        zoomXPower = 0;
        zoomX = 1;
        zoomY = 1;
        painter = new PaintThread();
        painter.addObserver(this);
        add(getScrollBar());
        add(getTimeRuler());
        if (filename != null) {
            try {
                AudioInputStream audio = AudioSystem.getAudioInputStream(new FileInputStream(filename));
                AudioFormat format = audio.getFormat();
                channels = format.getChannels();
                samplesize = format.getSampleSizeInBits();
                framesize = format.getFrameSize();
                framepersec = format.getSampleRate();
                duration = audio.getFrameLength() / framepersec;
                audio.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            channels = 2;
            samplesize = 16;
            framesize = 4;
            framepersec = 44100;
            duration = 44100 / framepersec;
        }
        maxsamplevalue = Math.pow(2, samplesize - 1);
        views = new Viewport[channels];
        for (int i = 0; i < channels; i++) views[i] = new Viewport();
        enableEvents(AWTEvent.FOCUS_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
        invalidate();
    }
}
