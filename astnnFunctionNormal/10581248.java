class BackupThread extends Thread {
    public static void LogStart() {
        try {
            File soundFile = new File("." + File.separator + "sound" + File.separator + "START.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat format = ais.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            int readBytes = 0;
            byte[] data = new byte[128000];
            while (readBytes != -1) {
                readBytes = ais.read(data, 0, data.length);
                if (0 <= readBytes) {
                    line.write(data, 0, readBytes);
                }
            }
            line.drain();
            line.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (LineUnavailableException exception) {
            exception.printStackTrace();
        } catch (UnsupportedAudioFileException exception) {
            exception.printStackTrace();
        }
    }
}
