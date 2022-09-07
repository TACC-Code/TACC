class BackupThread extends Thread {
    public MemoryMappedSuffixArray(String suffixesFileName, Corpus corpus, int maxCacheSize) throws IOException, ClassNotFoundException {
        super(corpus, new Cache<Pattern, MatchedHierarchicalPhrases>(maxCacheSize), new Cache<Pattern, List<Rule>>(maxCacheSize));
        RandomAccessFile binaryFile = new RandomAccessFile(suffixesFileName, "r");
        FileChannel binaryChannel = binaryFile.getChannel();
        IntBuffer tmp = binaryChannel.map(FileChannel.MapMode.READ_ONLY, 0, 4).asIntBuffer().asReadOnlyBuffer();
        size = tmp.get();
        if (size != corpus.size()) {
            throw new MismatchedCorpusException(this);
        }
        this.binarySuffixBuffer = binaryChannel.map(FileChannel.MapMode.READ_ONLY, 4, 4 * size).asIntBuffer().asReadOnlyBuffer();
    }
}
