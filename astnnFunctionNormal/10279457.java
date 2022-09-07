class BackupThread extends Thread {
    public DiskLogFile(String filename) throws IOException, LogEntryException {
        file = new File(filename);
        fis = new FileInputStream(file);
        channel = fis.getChannel();
        myInt = ByteBuffer.allocate(Integer.SIZE / 8);
        csumAlgo = new CRC32();
        next = getNext();
    }
}
