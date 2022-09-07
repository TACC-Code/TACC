class BackupThread extends Thread {
    public MappedFileBuffer(File file, int segmentSize, boolean readWrite) throws IOException {
        if (segmentSize > MAX_SEGMENT_SIZE) throw new IllegalArgumentException("segment size too large (max is " + MAX_SEGMENT_SIZE + "): " + segmentSize);
        _file = file;
        _isWritable = readWrite;
        _segmentSize = segmentSize;
        RandomAccessFile mappedFile = null;
        try {
            String mode = readWrite ? "rw" : "r";
            MapMode mapMode = readWrite ? MapMode.READ_WRITE : MapMode.READ_ONLY;
            mappedFile = new RandomAccessFile(file, mode);
            FileChannel channel = mappedFile.getChannel();
            long fileSize = file.length();
            int bufArraySize = (int) (fileSize / segmentSize) + ((fileSize % segmentSize != 0) ? 1 : 0);
            _buffers = new MappedByteBuffer[bufArraySize];
            int bufIdx = 0;
            for (long offset = 0; offset < fileSize; offset += segmentSize) {
                long remainingFileSize = fileSize - offset;
                long thisSegmentSize = Math.min(2L * segmentSize, remainingFileSize);
                _buffers[bufIdx++] = channel.map(mapMode, offset, thisSegmentSize);
            }
        } finally {
            IOUtil.closeQuietly(mappedFile);
        }
    }
}
