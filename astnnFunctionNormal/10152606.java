class BackupThread extends Thread {
    public void addAll(List<E> elements) {
        if (elements != null) {
            int newElementCount = elements.size();
            if (newElementCount > 0) {
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
                    long[] offsets = new long[elements.size()];
                    int index = 0;
                    for (E element : elements) {
                        offsets[index] = offset;
                        offset = offset + internalWriteElement(randomSerializeFile, offset, element) + 4;
                        index++;
                    }
                    index = 0;
                    for (long curOffset : offsets) {
                        internalWriteOffset(randomSerializeIndexFile, elementsCount + index, curOffset);
                        index++;
                    }
                } catch (Throwable e) {
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
    }
}
