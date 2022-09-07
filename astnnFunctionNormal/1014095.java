class BackupThread extends Thread {
    public boolean lock(File file, boolean createNewIfNotExist) {
        try {
            this.file = file;
            if (createNewIfNotExist && !file.exists()) {
                file.createNewFile();
            }
            this.fileChannel = new RandomAccessFile(file, "rw").getChannel();
            fileLock = fileChannel.tryLock();
        } catch (Exception e) {
            canLock = false;
            e.printStackTrace();
        }
        if (fileLock == null) {
            canLock = false;
        } else {
            canLock = true;
        }
        return canLock;
    }
}
