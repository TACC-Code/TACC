class BackupThread extends Thread {
    public void write(ByteBuffer buffer, long pos) throws ManagedFileException {
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException exp) {
            Thread.currentThread().interrupt();
            throw new ManagedFileException("write failes: interrupted", exp);
        }
        try {
            checkOpenFile();
            if (raFile == null) {
                throw new ManagedFileException("write failes: raFile null");
            }
            FileChannel channel = raFile.getChannel();
            if (!channel.isOpen()) {
                throw new ManagedFileException("write failes: not open");
            }
            channel.position(pos);
            int tryCount = 0;
            while (buffer.position() != buffer.limit()) {
                int written = channel.write(buffer.internalBuffer());
                if (written > 0) {
                    tryCount = 0;
                } else {
                    if (tryCount >= MAX_WRITE_TRIES) {
                        throw new ManagedFileException("write failes: max retries");
                    }
                    try {
                        Thread.sleep(WRITE_RETRY_DELAY * tryCount);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new ManagedFileException("write failes: interrupted");
                    }
                }
            }
        } catch (Exception exp) {
            throw new ManagedFileException("write fails", exp);
        } finally {
            lock.unlock();
        }
    }
}
