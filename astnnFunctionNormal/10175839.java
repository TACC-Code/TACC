class BackupThread extends Thread {
                public void run() {
                    AudioInputStream inStrom = null;
                    AudioFormat format = null;
                    try {
                        String path = FileHandler.getAbsolutePath(ExhibitionDiagramEditorPlugin.ID, "/files/sounds/Moeb.wav");
                        File datei = new File(path);
                        inStrom = AudioSystem.getAudioInputStream(datei);
                        format = inStrom.getFormat();
                        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                            AudioFormat neu = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 2 * format.getSampleSizeInBits(), format.getChannels(), 2 * format.getFrameSize(), format.getFrameRate(), true);
                            inStrom = AudioSystem.getAudioInputStream(neu, inStrom);
                            format = neu;
                        }
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    SourceDataLine line = null;
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                    try {
                        line = (SourceDataLine) AudioSystem.getLine(info);
                        line.open(format);
                        line.start();
                        int num = 0;
                        byte[] audioPuffer = new byte[5000];
                        while (num != -1) {
                            try {
                                num = inStrom.read(audioPuffer, 0, audioPuffer.length);
                                if (num >= 0) line.write(audioPuffer, 0, num);
                            } catch (Exception ex) {
                                System.out.println(ex);
                            }
                        }
                        line.drain();
                        line.close();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
}
