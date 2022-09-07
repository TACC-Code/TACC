class BackupThread extends Thread {
    public void backup() {
        log.fine("Creating backup of file " + file.getAbsolutePath());
        File backupFile = new File(file.getAbsolutePath() + ".old");
        if (backupFile.exists()) {
            backupFile.delete();
        }
        try {
            FileUtils.copyFile(file, backupFile);
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed to create backup file", e);
        }
    }
}
