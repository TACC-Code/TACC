class BackupThread extends Thread {
    public void write(String fileName) throws IOException {
        RandomAccessFile rf = new RandomAccessFile(fileName, "rw");
        FileChannel ch = rf.getChannel();
        int fileLength = dataSize();
        rf.setLength(fileLength);
        MappedByteBuffer buffer = ch.map(FileChannel.MapMode.READ_WRITE, 0, fileLength);
        write(buffer);
        buffer.force();
        ch.close();
    }
}
