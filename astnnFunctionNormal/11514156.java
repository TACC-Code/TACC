class BackupThread extends Thread {
    public static MetadataBlockHeader readHeader(RandomAccessFile raf) throws IOException {
        ByteBuffer rawdata = ByteBuffer.allocate(HEADER_LENGTH);
        int bytesRead = raf.getChannel().read(rawdata);
        if (bytesRead < HEADER_LENGTH) {
            throw new IOException("Unable to read required number of databytes read:" + bytesRead + ":required:" + HEADER_LENGTH);
        }
        rawdata.rewind();
        return new MetadataBlockHeader(rawdata);
    }
}
