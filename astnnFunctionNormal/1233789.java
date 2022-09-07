class BackupThread extends Thread {
    public int write(Buffer pkt, int blockNum, int stripeNum) throws IOException, FileAlreadyDecodedException {
        int result = -1;
        try {
            locks[blockNum].writeLock().acquire();
            try {
                result = write0(pkt, blockNum, stripeNum);
            } finally {
                locks[blockNum].writeLock().release();
            }
        } catch (InterruptedException e) {
            throw new InterruptedIOException(e.toString());
        }
        if (result >= k) {
            Thread.yield();
        }
        return result;
    }
}
