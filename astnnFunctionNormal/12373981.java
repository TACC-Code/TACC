class BackupThread extends Thread {
    public void delete(RandomAccessFile file) throws IOException {
        logger.info("Deleting ID3v1 from file if exists");
        FileChannel fc;
        ByteBuffer byteBuffer;
        fc = file.getChannel();
        if (file.length() < TAG_LENGTH) {
            throw new IOException("File not not appear large enough to contain a tag");
        }
        fc.position(file.length() - TAG_LENGTH);
        byteBuffer = ByteBuffer.allocate(TAG_LENGTH);
        fc.read(byteBuffer);
        byteBuffer.rewind();
        if (AbstractID3v1Tag.seekForV1OrV11Tag(byteBuffer)) {
            logger.config("Deleted ID3v1 tag");
            file.setLength(file.length() - TAG_LENGTH);
        } else {
            logger.config("Unable to find ID3v1 tag to deleteField");
        }
    }
}
