class BackupThread extends Thread {
    static boolean SNDDMA_Init() {
        if (snd_inited) return true;
        if (sndbits == null) {
            sndbits = Cvar.Get("sndbits", "16", CVAR_ARCHIVE);
            sndspeed = Cvar.Get("sndspeed", "0", CVAR_ARCHIVE);
            sndchannels = Cvar.Get("sndchannels", "1", CVAR_ARCHIVE);
        }
        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 22050, 16, 1, 2, 22050, false);
        DataLine.Info dinfo = new DataLine.Info(SourceDataLine.class, format);
        try {
            line = (SourceDataLine) AudioSystem.getLine(dinfo);
        } catch (LineUnavailableException e4) {
            return false;
        }
        dma.buffer = new byte[65536];
        dma.channels = format.getChannels();
        dma.samplebits = format.getSampleSizeInBits();
        dma.samples = dma.buffer.length / format.getFrameSize();
        dma.speed = (int) format.getSampleRate();
        dma.submission_chunk = 1;
        try {
            line.open(format, 4096);
        } catch (LineUnavailableException e5) {
            return false;
        }
        line.start();
        thread = new SoundThread(dma.buffer, line);
        thread.start();
        snd_inited = true;
        return true;
    }
}
