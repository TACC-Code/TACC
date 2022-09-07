class BackupThread extends Thread {
    public boolean tryLock() {
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            FileChannel channel = randomAccessFile.getChannel();
            try {
                lock = channel.tryLock();
            } catch (OverlappingFileLockException e) {
                return false;
            }
            if (lock == null) {
                return false;
            }
        } catch (FileNotFoundException e) {
            LOG.warning(e.getMessage());
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return true;
    }
}
