class BackupThread extends Thread {
    private void init() throws PlayerException {
        if (!supports(format)) throw new PlayerException("data format not supported: " + format);
        af = new AudioFormat(format.getRate(), format.getBits(), format.getChannels(), signed, bigendian);
        DataLine.Info dli = new DataLine.Info(SourceDataLine.class, af);
        if (AudioSystem.isLineSupported(dli)) {
            Line l = null;
            try {
                l = AudioSystem.getLine(dli);
            } catch (LineUnavailableException e) {
                throw new PlayerException("Can't get line", e);
            }
            if (!(l instanceof SourceDataLine)) throw new PlayerException("line not a SourceDataLine!");
            sdl = (SourceDataLine) l;
        } else throw new PlayerException("Line not supported");
        if (sdl == null) throw new PlayerException("line not found");
    }
}
