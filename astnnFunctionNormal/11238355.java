class BackupThread extends Thread {
    protected void loadInfo(AudioFileFormat aff) throws UnsupportedAudioFileException {
        type = aff.getType().toString();
        if (!type.equalsIgnoreCase("flac")) {
            throw new UnsupportedAudioFileException("Not Flac audio format");
        }
        channels = info.getChannels();
        samplerate = info.getSampleRate();
        bitspersample = info.getBitsPerSample();
        duration = Math.round(info.getTotalSamples() / info.getSampleRate());
    }
}
