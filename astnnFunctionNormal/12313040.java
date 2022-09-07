class BackupThread extends Thread {
    public MemoryMappedCorpusArray(Vocabulary vocabulary, String binaryCorpusFileName, int binaryCorpusFileSize, String binarySentenceFileName, int binarySentenceFileSize) throws IOException {
        this.vocabulary = vocabulary;
        RandomAccessFile binarySentenceFile = new RandomAccessFile(binarySentenceFileName, "r");
        FileChannel binarySentenceFileChannel = binarySentenceFile.getChannel();
        this.binarySentenceBuffer = binarySentenceFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, binarySentenceFileSize);
        RandomAccessFile binaryCorpusFile = new RandomAccessFile(binaryCorpusFileName, "r");
        FileChannel binaryCorpusFileChannel = binaryCorpusFile.getChannel();
        this.binaryCorpusBuffer = binaryCorpusFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, binaryCorpusFileSize);
        this.numberOfWords = binaryCorpusFileSize;
        this.numberOfSentences = binarySentenceFileSize;
    }
}
