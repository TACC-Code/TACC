class BackupThread extends Thread {
    public static Sound getSoundDefinition(InputStream audioFile, int framesPerSecond) throws IOException, UnsupportedAudioFileException {
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(audioFile));
        AudioFormat format = audioIn.getFormat();
        int frameSize = format.getFrameSize();
        boolean isStereo = format.getChannels() == 2;
        boolean is16bit = format.getSampleSizeInBits() > 8;
        int sampleRate = (int) format.getSampleRate();
        int rate = SWFConstants.SOUND_FREQ_5_5KHZ;
        if (sampleRate >= 44000) sampleRate = 44000; else if (sampleRate >= 22000) sampleRate = 22000; else if (sampleRate >= 11000) sampleRate = 11000; else sampleRate = 5500;
        if (sampleRate == 44000) rate = SWFConstants.SOUND_FREQ_44KHZ; else if (sampleRate == 22000) rate = SWFConstants.SOUND_FREQ_22KHZ; else if (sampleRate == 11000) rate = SWFConstants.SOUND_FREQ_11KHZ;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ADPCMHelper.FramedInputStream in = new ADPCMHelper.FramedInputStream(audioIn, frameSize);
        int b = 0;
        int count = 0;
        while ((b = in.read()) >= 0) {
            count++;
            bout.write(b);
        }
        int sampleCount = count;
        if (is16bit) sampleCount /= 2;
        if (isStereo) sampleCount /= 2;
        byte[] soundData = bout.toByteArray();
        return new Sound(SWFConstants.SOUND_FORMAT_RAW, rate, is16bit, isStereo, sampleCount, soundData);
    }
}
