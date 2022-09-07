class BackupThread extends Thread {
    public MP3Reader(File file) throws FileNotFoundException {
        this.file = file;
        fis = new FileInputStream(file);
        channel = fis.getChannel();
        try {
            mappedFile = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        } catch (IOException e) {
            log.error("MP3Reader {}", e);
        }
        mappedFile.order(ByteOrder.BIG_ENDIAN);
        in = IoBuffer.wrap(mappedFile);
        analyzeKeyFrames();
        firstTags.addFirst(createFileMeta());
        if (in.remaining() > 4) {
            searchNextFrame();
            int pos = in.position();
            MP3Header header = readHeader();
            in.position(pos);
            if (header != null) {
                checkValidHeader(header);
            } else {
                throw new RuntimeException("No initial header found.");
            }
        }
    }
}
