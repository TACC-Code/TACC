class BackupThread extends Thread {
    protected void clearPreviousDownloadedCsv() {
        String portfolioCsvFile = getCsvFilePath();
        String portfolioBackupFolder = PropsUtil.get("portfolio.backup.folder.path");
        File backupFolder = new File(portfolioBackupFolder);
        if (!backupFolder.isDirectory() && !backupFolder.exists()) {
            logger.info("Creating backup dir " + portfolioBackupFolder);
            backupFolder.mkdirs();
        }
        File csvFile = new File(portfolioCsvFile);
        if (csvFile.exists()) {
            logger.info("Moving file " + csvFile.getAbsolutePath() + " to " + portfolioBackupFolder);
            try {
                String pathname = backupFolder + "/" + DateUtil.getSimpleTimePath() + csvFile.getName();
                logger.info(pathname);
                FileUtils.copyFile(csvFile, new File(pathname));
            } catch (IOException e) {
                logger.error(e, e);
            }
            csvFile.delete();
        }
    }
}
