class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        Microphone mic = new Java2MacMic();
        AudioFormat micFormat = AudioUtil.PCM_16K_MONO;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, micFormat);
        SourceDataLine line = null;
        try {
            System.out.println("Java2Speaker.open about to getLine: " + micFormat);
            line = (SourceDataLine) AudioSystem.getLine(info);
            System.out.println("Java2Speaker.open returned from getLine" + " about to line.open: " + line);
            System.out.println("line.isActive(): " + line.isActive());
            System.out.println("line.isOpen(): " + line.isOpen());
            System.out.println("line.isRunning(): " + line.isRunning());
            line.open(micFormat);
            line.start();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        byte[] baAudio = new byte[(int) micFormat.getFrameRate()];
        if (!mic.open((int) micFormat.getSampleRate(), micFormat.getChannels(), micFormat.getSampleSizeInBits(), (int) micFormat.getSampleRate(), 2)) {
            line.close();
            return;
        }
        for (int i = 0; i < 40; ) {
            mic.getBuffer(baAudio, 0, baAudio.length);
            System.out.println(line.write(baAudio, 0, baAudio.length));
        }
        mic.close();
    }
}
