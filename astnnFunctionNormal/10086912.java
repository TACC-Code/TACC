class BackupThread extends Thread {
    public static void createVolume(String aVolumeFileName, int aPageSize, long aDatabaseID, long aLogicalFirstPageOffset, long aMaximumSize, long aPreAllocatedSize) throws PageServerException {
        File volumeFile = new File(aVolumeFileName);
        if (volumeFile.exists()) {
            if (volumeFile.isFile() || volumeFile.isDirectory()) {
                throw new PageServerException("File already exists: " + aVolumeFileName);
            }
        }
        if (aPageSize < MIN_PAGE_SIZE || aPageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Page Size out of range: " + aPageSize);
        }
        aLogicalFirstPageOffset = ((aLogicalFirstPageOffset + aPageSize - 1) / aPageSize) * aPageSize;
        aMaximumSize = ((aMaximumSize + aPageSize - 1) / aPageSize) * aPageSize;
        aPreAllocatedSize = ((aPreAllocatedSize + aPageSize - 1) / aPageSize) * aPageSize;
        if (aLogicalFirstPageOffset < NULL_OFFSET) {
            throw new IllegalArgumentException("First page offset out of range: " + aLogicalFirstPageOffset);
        }
        RandomAccessFile volume = null;
        boolean success = false;
        FileLock lock = null;
        try {
            volume = new RandomAccessFile(volumeFile, "rw");
            lock = volume.getChannel().tryLock();
            if (lock == null) {
                throw new PageServerException("Cannot lock volume: " + volumeFile);
            }
            VolumeHeader hdr = new VolumeHeader(aPageSize, aDatabaseID, aLogicalFirstPageOffset, aMaximumSize);
            hdr.write(volume);
            if (aPreAllocatedSize != 0) {
                final int bufSize = 8192;
                byte[] buf = new byte[bufSize];
                long startOffset = hdr.getPhysicalFirstPageOffset();
                long bytesLeft = aPreAllocatedSize - startOffset;
                volume.seek(startOffset);
                while (bytesLeft > 0) {
                    int numToWrite = (int) (bufSize > bytesLeft ? bytesLeft : bufSize);
                    volume.write(buf, 0, numToWrite);
                    bytesLeft -= numToWrite;
                }
            }
            success = true;
        } catch (IOException e) {
            throw new PageServerException("Error creating volume: " + e, e);
        } finally {
            if (volume != null) {
                try {
                    if (lock != null) {
                        lock.release();
                    }
                    volume.close();
                    volume = null;
                } catch (IOException e) {
                    if (success) {
                        throw new PageServerException("Error closing volume: " + e, e);
                    }
                }
            }
            if (!success) {
                volumeFile.delete();
            }
        }
    }
}
