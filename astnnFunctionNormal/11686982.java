class BackupThread extends Thread {
    @Override
    protected boolean tryLockImpl() {
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            fileLock = randomAccessFile.getChannel().tryLock();
            if (fileLock != null) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("SLOCK " + System.currentTimeMillis() + " EEE " + getName() + " - Unable to create and/or lock file");
            e.printStackTrace();
        }
        return false;
    }
}
