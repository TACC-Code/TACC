class BackupThread extends Thread {
    private void backupFile() throws IOException {
        log.debug("filePath = " + filePath);
        File backupFile = new File(ctx.backupFolder + "/" + fileName);
        if (!backupFile.exists()) FileUtils.copyFile(new File(filePath), backupFile);
    }
}
