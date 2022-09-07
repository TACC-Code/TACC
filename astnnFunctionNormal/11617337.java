class BackupThread extends Thread {
    protected static void buildLines() throws Exception {
        Vector vOptimal = new Vector();
        Vector vAll = new Vector();
        Info[] tl = AudioSystem.getTargetLineInfo(new Info(TargetDataLine.class));
        for (int k = 0; k < tl.length; k++) {
            if (tl[k] instanceof javax.sound.sampled.DataLine.Info) {
                AudioFormat[] formats = ((javax.sound.sampled.DataLine.Info) tl[k]).getFormats();
                for (int j = 0; j < formats.length; j++) {
                    AudioFormat f = formats[j];
                    if (f.getSampleRate() == AudioSystem.NOT_SPECIFIED || f.getSampleRate() >= 8000.0F) vAll.add(f);
                    if (f.getChannels() == CHANNELS && f.getSampleSizeInBits() == BITS && (f.getSampleRate() == AudioSystem.NOT_SPECIFIED || f.getSampleRate() == RATE)) vOptimal.add(f);
                }
            }
        }
        if (vAll.isEmpty()) {
            throw new Exception("Unable to find any available TargetDataLine for recording");
        }
        javax.sound.sampled.DataLine.Info dli;
        AudioFormat[] af;
        if (!vOptimal.isEmpty()) {
            af = (AudioFormat[]) vOptimal.toArray(new AudioFormat[vOptimal.size()]);
            dli = new javax.sound.sampled.DataLine.Info(TargetDataLine.class, af, LINE_BUFFER, LINE_BUFFER + 1000);
            try {
                m_targetLine = (TargetDataLine) AudioSystem.getLine(dli);
                m_targetLine.open();
            } catch (Exception ex) {
                m_targetLine = null;
            }
        }
        if (m_targetLine == null) {
            af = (AudioFormat[]) vAll.toArray(new AudioFormat[vAll.size()]);
            dli = new javax.sound.sampled.DataLine.Info(TargetDataLine.class, af, LINE_BUFFER, LINE_BUFFER + 1000);
            m_targetLine = (TargetDataLine) AudioSystem.getLine(dli);
            m_targetLine.open();
        }
        dli = new javax.sound.sampled.DataLine.Info(SourceDataLine.class, m_targetLine.getFormat());
        m_sourceLine = (SourceDataLine) AudioSystem.getLine(dli);
        m_sourceLine.open();
        initialized = true;
    }
}
