class BackupThread extends Thread {
    public static void copyFile(File srcFile, File destFile) {
        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            LOGGER.error("Unable to copy file " + srcFile.getName(), e);
        }
    }
}
