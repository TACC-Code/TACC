class BackupThread extends Thread {
    public void initialize(File repositoryDir) {
        File baseIdFile = new File(repositoryDir, ID_FILE);
        if (!baseIdFile.exists()) {
            throw new RuntimeException("Repository id file not found.");
        }
        try {
            idFile = new RandomAccessFile(baseIdFile, "rwd");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to open repository id file.", e);
        }
        FileChannel lockChannel = idFile.getChannel();
        try {
            repositoryLock = lockChannel.tryLock();
        } catch (IOException e) {
            throw new RuntimeException("Failed to obtain lock on repository.", e);
        }
        if (repositoryLock == null) {
            throw new RuntimeException("Failed to obtain lock on repository; repository is already locked.");
        }
        try {
            idFile.seek(0);
            nextItemId = idFile.readInt();
        } catch (IOException e) {
            try {
                repositoryLock.release();
            } catch (IOException ne) {
                log.warn("Error occurred releasing lock after id read failure.", ne);
            }
            repositoryLock = null;
            throw new RuntimeException("Failed to read next repository item id.", e);
        }
        lockIdSalt = System.currentTimeMillis();
    }
}
