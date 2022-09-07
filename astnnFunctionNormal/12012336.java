class BackupThread extends Thread {
    public BufferedRAFile(String path, boolean onlyRead, long bufferLen) throws FileNotFoundException {
        readOnly = onlyRead;
        bufferLength = bufferLen;
        file = new RandomAccessFile(path, readOnly ? "r" : "rw");
        this.channel = file.getChannel();
        try {
            buffer = channel.map(readOnly ? FileChannel.MapMode.READ_ONLY : FileChannel.MapMode.READ_WRITE, 0, bufferLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
