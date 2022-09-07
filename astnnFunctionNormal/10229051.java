class BackupThread extends Thread {
    public BTree(File dataFile, int blockSize, int valueSize, RecordComparator comparator, boolean forceSync) throws IOException {
        if (dataFile == null) {
            throw new IllegalArgumentException("dataFile must not be null");
        }
        if (blockSize < HEADER_LENGTH) {
            throw new IllegalArgumentException("block size must be at least " + HEADER_LENGTH + " bytes");
        }
        if (valueSize <= 0) {
            throw new IllegalArgumentException("value size must be larger than 0");
        }
        if (blockSize < 3 * valueSize + 20) {
            throw new IllegalArgumentException("block size to small; must at least be able to store three values");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("comparator muts not be null");
        }
        this.file = dataFile;
        this.comparator = comparator;
        this.forceSync = forceSync;
        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (!created) {
                throw new IOException("Failed to create file: " + file);
            }
        }
        raf = new RandomAccessFile(file, "rw");
        fileChannel = raf.getChannel();
        if (fileChannel.size() == 0L) {
            this.blockSize = blockSize;
            this.valueSize = valueSize;
            this.rootNodeID = 0;
            this.maxNodeID = 0;
            writeFileHeader();
            sync();
        } else {
            ByteBuffer buf = ByteBuffer.allocate(HEADER_LENGTH);
            fileChannel.read(buf, 0L);
            buf.rewind();
            @SuppressWarnings("unused") int fileFormatVersion = buf.getInt();
            this.blockSize = buf.getInt();
            this.valueSize = buf.getInt();
            this.rootNodeID = buf.getInt();
            if (this.valueSize != valueSize) {
                throw new IOException("Specified value size (" + valueSize + ") is different from what is stored on disk (" + this.valueSize + ")");
            }
        }
        slotSize = 4 + this.valueSize;
        branchFactor = 1 + (this.blockSize - 8) / slotSize;
        minValueCount = (branchFactor - 1) / 2;
        nodeSize = 8 + (branchFactor - 1) * slotSize;
        maxNodeID = Math.max(0, offset2nodeID(fileChannel.size() - nodeSize));
    }
}
