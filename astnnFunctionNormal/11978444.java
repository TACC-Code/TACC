class BackupThread extends Thread {
    public static Font readFont(String fileName) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(fileName, "r");
        FileChannel ch = raf.getChannel();
        MappedByteBuffer buffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, raf.length());
        Font f = new Font(buffer);
        ch.close();
        return f;
    }
}
