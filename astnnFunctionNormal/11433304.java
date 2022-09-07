class BackupThread extends Thread {
    private void readV2Tag(File file, int loadOptions) throws IOException, TagException {
        int startByte = (int) ((MP3AudioHeader) audioHeader).getMp3StartByte();
        if (startByte >= AbstractID3v2Tag.TAG_HEADER_LENGTH) {
            logger.finer("Attempting to read id3v2tags");
            FileInputStream fis = null;
            FileChannel fc = null;
            ByteBuffer bb = null;
            try {
                fis = new FileInputStream(file);
                fc = fis.getChannel();
                bb = ByteBuffer.allocate(startByte);
                fc.read(bb);
            } finally {
                if (fc != null) {
                    fc.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }
            bb.rewind();
            if ((loadOptions & LOAD_IDV2TAG) != 0) {
                logger.info("Attempting to read id3v2tags");
                try {
                    this.setID3v2Tag(new ID3v24Tag(bb, file.getName()));
                } catch (TagNotFoundException ex) {
                    logger.info("No id3v24 tag found");
                }
                try {
                    if (id3v2tag == null) {
                        this.setID3v2Tag(new ID3v23Tag(bb, file.getName()));
                    }
                } catch (TagNotFoundException ex) {
                    logger.info("No id3v23 tag found");
                }
                try {
                    if (id3v2tag == null) {
                        this.setID3v2Tag(new ID3v22Tag(bb, file.getName()));
                    }
                } catch (TagNotFoundException ex) {
                    logger.info("No id3v22 tag found");
                }
            }
        } else {
            logger.info("Not enough room for valid id3v2 tag:" + startByte);
        }
    }
}
