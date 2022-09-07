class BackupThread extends Thread {
    @Override
    public void load(File input) throws IOException, UnsupportedAudioFileException {
        AudioFileFormat aff = AudioSystem.getAudioFileFormat(input);
        type = aff.getType().toString();
        if (!type.equalsIgnoreCase("flac")) {
            throw new UnsupportedAudioFileException("Not Flac audio format");
        }
        size = input.length();
        location = input.getPath();
        FileInputStream is = new FileInputStream(input);
        FLACDecoder decoder = new FLACDecoder(is);
        decoder.readMetadata();
        info = decoder.getStreamInfo();
        FlacTag flacTag = null;
        GenericAudioHeader gah = null;
        try {
            AudioFile flacFile = AudioFileIO.read(input);
            flacTag = (FlacTag) flacFile.getTag();
            FlacInfoReader fir = new FlacInfoReader();
            gah = fir.read(new RandomAccessFile(input, "r"));
            if (gah != null) {
                type = gah.getEncodingType();
                channels = gah.getChannelNumber();
                samplerate = gah.getSampleRateAsNumber();
                bitrate = (int) gah.getBitRateAsNumber();
                duration = gah.getTrackLength();
            }
            if (flacTag != null) {
                title = flacTag.getFirstTitle();
                artist = flacTag.getFirstArtist();
                album = flacTag.getFirstAlbum();
                year = flacTag.getFirstYear();
                genre = flacTag.getFirstGenre();
                track = flacTag.getFirstTrack();
                comment = flacTag.getFirstComment();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
