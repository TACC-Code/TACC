class BackupThread extends Thread {
    public PSLReader(String filename) throws IOException {
        super(filename);
        logFile = new RandomAccessFile(filename, "r");
        bb = ByteBuffer.allocate((int) logFile.length());
        fc = logFile.getChannel();
        fc.read(bb);
        packets = new FastList<DataPacket>();
    }
}
