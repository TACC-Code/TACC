class BackupThread extends Thread {
    public int read(ByteBuffer buffer, long pos) throws ManagedFileException {
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException exp) {
            Thread.currentThread().interrupt();
            throw new ManagedFileException("read failes: interrupted", exp);
        }
        try {
            checkOpenFile();
            if (raFile == null) {
                throw new ManagedFileException("read failes: raFile null");
            }
            FileChannel channel = raFile.getChannel();
            if (!channel.isOpen()) {
                throw new ManagedFileException("read failes: not open");
            }
            channel.position(pos);
            int totalRead = 0;
            int read;
            while (channel.position() < channel.size() && buffer.hasRemaining()) {
                read = channel.read(buffer.internalBuffer());
                if (read > 0) {
                    totalRead += read;
                }
            }
            return totalRead;
        } catch (Exception exp) {
            throw new ManagedFileException("read fails", exp);
        } finally {
            lock.unlock();
        }
    }
}
