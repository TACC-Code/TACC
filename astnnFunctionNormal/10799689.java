class BackupThread extends Thread {
    public DoubleFileChannel(final String sourceFileName, final SampleSign sign, final ByteOrder order) throws IOException {
        this.file = new RandomAccessFile(sourceFileName, "r");
        if ((this.file.length() % 8) != 0) {
            file.close();
            throw new IOException("File length does not match");
        }
        this.channel = file.getChannel();
        this.sign = sign;
        this.order = order;
    }
}
