class BackupThread extends Thread {
    void exportVideo(ResourcePlace video, ResourcePlace audio, final String fileNameBase, final JLabel currentLabel, final JProgressBar currentProgress) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                currentProgress.setValue(0);
                currentLabel.setText("Current file: " + fileNameBase);
            }
        });
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(new GZIPInputStream(video.open(), 1024 * 1024), 1024 * 1024));
            try {
                int w = Integer.reverseBytes(in.readInt());
                int h = Integer.reverseBytes(in.readInt());
                final int frames = Integer.reverseBytes(in.readInt());
                double fps = Integer.reverseBytes(in.readInt()) / 1000.0;
                int[] palette = new int[256];
                byte[] bytebuffer = new byte[w * h];
                int[] currentImage = new int[w * h];
                int frameCount = 0;
                int frames2 = frames;
                if (audio != null) {
                    try {
                        AudioInputStream ain = AudioSystem.getAudioInputStream(new BufferedInputStream(audio.open(), 256 * 1024));
                        try {
                            frames2 = (int) Math.ceil(ain.available() * fps / 22050.0);
                        } finally {
                            ain.close();
                        }
                    } catch (UnsupportedAudioFileException ex) {
                        ex.printStackTrace();
                    }
                }
                final int f = Math.max(frames, frames2);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        currentProgress.setMaximum(f);
                        currentLabel.setText("Current file: " + fileNameBase + ", 0 of " + f);
                    }
                });
                if (audio != null) {
                    String audioNaming = fileNameBase.substring(0, fileNameBase.length() - 9) + ".wav";
                    InputStream asrc = audio.open();
                    try {
                        FileOutputStream acopy = new FileOutputStream(audioNaming);
                        try {
                            byte[] ab = new byte[1024 * 1024];
                            while (!Thread.currentThread().isInterrupted()) {
                                int read = asrc.read(ab);
                                if (read < 0) {
                                    break;
                                } else if (read > 0) {
                                    acopy.write(ab, 0, read);
                                }
                            }
                        } finally {
                            acopy.close();
                        }
                    } finally {
                        asrc.close();
                    }
                }
                BufferedImage frameImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                while (!Thread.currentThread().isInterrupted()) {
                    int c = in.read();
                    if (c < 0 || c == 'X') {
                        break;
                    } else if (c == 'P') {
                        int len = in.read();
                        for (int j = 0; j < len; j++) {
                            int r = in.read() & 0xFF;
                            int g = in.read() & 0xFF;
                            int b = in.read() & 0xFF;
                            palette[j] = 0xFF000000 | (r << 16) | (g << 8) | b;
                        }
                    } else if (c == 'I') {
                        in.readFully(bytebuffer);
                        for (int i = 0; i < bytebuffer.length; i++) {
                            int c0 = palette[bytebuffer[i] & 0xFF];
                            if (c0 != 0) {
                                currentImage[i] = c0;
                            }
                        }
                        frameImage.setRGB(0, 0, w, h, currentImage, 0, w);
                        ImageIO.write(frameImage, "png", new File(String.format(fileNameBase, frameCount)));
                        frameCount++;
                        final int fc = frameCount;
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                currentProgress.setValue(fc);
                                currentLabel.setText("Current file: " + String.format(fileNameBase, fc) + ", " + fc + " of " + f);
                            }
                        });
                    }
                }
                if (frames2 > frames && !Thread.currentThread().isInterrupted()) {
                    for (int i = frames; i < frames2 && !Thread.currentThread().isInterrupted(); i++) {
                        ImageIO.write(frameImage, "png", new File(String.format(fileNameBase, frameCount)));
                        frameCount++;
                        final int fc = frameCount;
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                currentProgress.setValue(fc);
                                currentLabel.setText("Current file: " + String.format(fileNameBase, fc) + ", " + fc + " of " + f);
                            }
                        });
                    }
                }
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
