class BackupThread extends Thread {
    public void add(E element) {
        RandomAccessFile randomSerializeIndexFile = null;
        RandomAccessFile randomSerializeFile = null;
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        Throwable throwable = null;
        try {
            randomSerializeIndexFile = new RandomAccessFile(indexFile, "rw");
            randomSerializeFile = new RandomAccessFile(dataFile, "rw");
            long elementsCount = internalGetSize(randomSerializeIndexFile);
            long offset = 0;
            if (elementsCount > 0) {
                long prevElement = elementsCount - 1;
                offset = internalOffsetOfElement(randomSerializeIndexFile, prevElement);
                offset = offset + internalReadElementSize(randomSerializeFile, offset) + 4;
            }
            internalWriteElement(randomSerializeFile, offset, element);
            internalWriteOffset(randomSerializeIndexFile, elementsCount, offset);
        } catch (IOException e) {
            throwable = e;
        } finally {
            closeQuietly(randomSerializeFile);
            closeQuietly(randomSerializeIndexFile);
            lock.unlock();
        }
        if (throwable != null) {
            if (logger.isWarnEnabled()) logger.warn("Couldn't write element!", throwable);
        }
    }
}
