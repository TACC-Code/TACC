class BackupThread extends Thread {
    public void writer__backup(String rFilePath, String rFileName, String rBackupFolder) throws Exception {
        File backupFolder = new File(rBackupFolder);
        if (!backupFolder.exists()) {
            if (!backupFolder.mkdir()) {
                throw new Exception(writer__UITEXT_BackupFolderColon + backupFolder + writer__UITEXT_BackupFolderExistFailure);
            }
        }
        if (!backupFolder.isDirectory()) {
            throw new Exception(writer__UITEXT_BackupFolderColon + backupFolder + writer__UITEXT_BackupFolderNotAFolder);
        }
        if (!backupFolder.canWrite()) {
            throw new Exception(writer__UITEXT_BackupFolderColon + backupFolder + writer__UITEXT_BackupFolderNotWritable);
        }
        Calendar calendar = Calendar.getInstance();
        String year_yyyy = _align(String.valueOf(calendar.get(Calendar.YEAR)), "0", 4, 'r');
        String month_mm = _align(String.valueOf((1 + calendar.get(Calendar.MONTH))), "0", 2, 'r');
        String day_dd = _align(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), "0", 2, 'r');
        String hour_hh = _align(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)), "0", 2, 'r');
        String minute_mm = _align(String.valueOf(calendar.get(Calendar.MINUTE)), "0", 2, 'r');
        String second_ss = _align(String.valueOf(calendar.get(Calendar.SECOND)), "0", 2, 'r');
        String dateTime = year_yyyy + month_mm + day_dd + hour_hh + minute_mm + second_ss;
        String backupFileName = dateTime + writer__iBackupPrefix + rFileName + writer__iBackupSuffix;
        File backupFilePath = new File(rBackupFolder, backupFileName);
        File fileToBackup = new File(rFilePath);
        if (fileToBackup.exists()) {
            String fileContents = writer__read(rFilePath);
            writer__save(backupFilePath.getPath(), fileContents);
        }
    }
}
