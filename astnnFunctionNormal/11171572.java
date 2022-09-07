class BackupThread extends Thread {
    public synchronized void openDataPointsFile(File dataPointsFileName) throws IOException {
        if (this.dataPointsFile != null) {
            throw new IOException("Cannot open another data points file, because one is already open");
        }
        this.dataPointsFileName = dataPointsFileName;
        this.dataPointsFile = new RandomAccessFile(dataPointsFileName, "rw");
        FileChannel fileChannel = dataPointsFile.getChannel();
        fileChannel.lock();
        dataPointsFileName.deleteOnExit();
    }
}
