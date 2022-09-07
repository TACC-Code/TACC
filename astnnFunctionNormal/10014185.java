class BackupThread extends Thread {
    public void configure(JobConf job) {
        try {
            localFiles = DistributedCache.getLocalCacheFiles(job);
            File inFile = new File(localFiles[0].toUri().getPath());
            raFile = new RandomAccessFile(inFile, "r");
            ioChannel = raFile.getChannel();
            buffer = ioChannel.map(FileChannel.MapMode.READ_ONLY, 0L, ioChannel.size()).load();
            blockSize = job.getInt(Common.CONFIG_BLOCK_SIZE, -1);
            wordSize = job.getInt(Common.CONFIG_WORD_SIZE, 1);
            outputPath = job.getStrings(Common.CONFIG_OUTPUT_PATH)[0];
            origFileSize = job.getInt(Common.INPUT_FILE_SIZE, -1);
            fs = FileSystem.get(job);
        } catch (IOException e) {
            e.printStackTrace();
            buffer = null;
        }
    }
}
