class BackupThread extends Thread {
    public void switchFiles() throws LogException {
        ++currentFile;
        String fname = getFileName(currentFile);
        File file = new File(dir, fname);
        if (file.exists()) {
            if (LOG.isDebugEnabled()) LOG.debug("Log file " + file.getAbsolutePath() + " already exists. Copying it.");
            boolean renamed = file.renameTo(new File(file.getAbsolutePath() + BAK_FILE_SUFFIX));
            if (renamed && LOG.isDebugEnabled()) LOG.debug("Old file renamed to " + file.getAbsolutePath());
            file = new File(dir, fname);
        }
        if (LOG.isDebugEnabled()) LOG.debug("Creating new log file: " + file.getAbsolutePath());
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            channel = raf.getChannel();
        } catch (FileNotFoundException e) {
            throw new LogException("Failed to open new log file: " + file.getAbsolutePath(), e);
        }
    }
}
