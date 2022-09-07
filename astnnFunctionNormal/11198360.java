class BackupThread extends Thread {
    public boolean isConversionNeeded(StorageDirectory sd) throws IOException {
        File oldF = new File(sd.getRoot(), "storage");
        if (!oldF.exists()) return false;
        RandomAccessFile oldFile = new RandomAccessFile(oldF, "rws");
        FileLock oldLock = oldFile.getChannel().tryLock();
        try {
            oldFile.seek(0);
            int oldVersion = oldFile.readInt();
            if (oldVersion < LAST_PRE_UPGRADE_LAYOUT_VERSION) return false;
        } finally {
            oldLock.release();
            oldFile.close();
        }
        return true;
    }
}
