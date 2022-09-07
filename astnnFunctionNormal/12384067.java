class BackupThread extends Thread {
    public ID3v1Tag(RandomAccessFile file, String loggingFilename) throws TagNotFoundException, IOException {
        setLoggingFilename(loggingFilename);
        FileChannel fc;
        ByteBuffer byteBuffer;
        fc = file.getChannel();
        fc.position(file.length() - TAG_LENGTH);
        byteBuffer = ByteBuffer.allocate(TAG_LENGTH);
        fc.read(byteBuffer);
        byteBuffer.flip();
        read(byteBuffer);
    }
}
