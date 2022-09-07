class BackupThread extends Thread {
    protected boolean readIntoBuffer(int minLength) {
        if (bufferSpaceLeft() < minLength) {
            increaseBufferSize(minLength + currentlyInBuffer());
        } else if (buf.length - writePos < minLength) {
            compact();
        }
        int readSum = 0;
        while (readSum < minLength && hasMoreData()) {
            prepareBlock();
            int blockSize = getBlockSize();
            if (buf.length < writePos + blockSize) {
                increaseBufferSize(writePos + blockSize);
            }
            int read = readBlock(buf, writePos);
            writePos += read;
            readSum += read;
        }
        if (dataProcessor != null) {
            dataProcessor.applyInline(buf, writePos - readSum, readSum);
        }
        return readSum >= minLength;
    }
}
