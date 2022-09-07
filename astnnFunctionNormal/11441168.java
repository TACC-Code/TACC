class BackupThread extends Thread {
    public static SoundStreamHead streamingBlocks(InputStream audioFile, int framesPerSecond, ArrayList soundBlocks) throws IOException, UnsupportedAudioFileException {
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioIn.getFormat();
        int frameSize = format.getFrameSize();
        boolean isStereo = format.getChannels() == 2;
        boolean is16Bit = format.getSampleSizeInBits() > 8;
        int sampleRate = (int) format.getSampleRate();
        int rate = SWFConstants.SOUND_FREQ_5_5KHZ;
        if (sampleRate >= 44000) sampleRate = 44000; else if (sampleRate >= 22000) sampleRate = 22000; else if (sampleRate >= 11000) sampleRate = 11000; else sampleRate = 5500;
        if (sampleRate == 44000) rate = SWFConstants.SOUND_FREQ_44KHZ; else if (sampleRate == 22000) rate = SWFConstants.SOUND_FREQ_22KHZ; else if (sampleRate == 11000) rate = SWFConstants.SOUND_FREQ_11KHZ;
        int samplesPerFrame = sampleRate / framesPerSecond;
        int blockSize = samplesPerFrame * (is16Bit ? 2 : 1) * (isStereo ? 2 : 1);
        SoundStreamHead soundHead = new SoundStreamHead(rate, is16Bit, isStereo, SWFConstants.SOUND_FORMAT_RAW, rate, is16Bit, isStereo, samplesPerFrame);
        if (soundBlocks == null) return soundHead;
        ByteArrayOutputStream bout = new ByteArrayOutputStream(blockSize + 1000);
        while (true) {
            bout.reset();
            byte[] b = new byte[frameSize];
            int num = 0;
            int read = 0;
            while (num < blockSize) {
                read = 0;
                while ((read = audioIn.read(b, read, frameSize - read)) < frameSize && read != -1) ;
                if (read == -1) break;
                bout.write(b);
                num += read;
            }
            while (num < blockSize) {
                bout.write(0x80);
                num++;
            }
            soundBlocks.add(bout.toByteArray());
            if (read == -1) break;
        }
        return soundHead;
    }
}
