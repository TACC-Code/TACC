class BackupThread extends Thread {
    private FileChannel getCacheFileChannel() throws IOException {
        if (this.cacheFile == null) {
            this.cacheFile = File.createTempFile("scalr.", ".s3");
        }
        return new RandomAccessFile(this.cacheFile, "rw").getChannel();
    }
}
