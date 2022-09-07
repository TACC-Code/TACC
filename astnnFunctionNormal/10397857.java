class BackupThread extends Thread {
    public MP4Reader(File f) throws IOException {
        if (null == f) {
            log.warn("Reader was passed a null file");
            log.debug("{}", ToStringBuilder.reflectionToString(this));
        }
        this.file = f;
        this.fis = new MP4DataStream(new FileInputStream(f));
        channel = fis.getChannel();
        decodeHeader();
        analyzeFrames();
        firstTags.add(createFileMeta());
        createPreStreamingTags(0, false);
    }
}
