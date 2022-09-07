class BackupThread extends Thread {
    public void copyFileToDirectory(final File srcFile, final File destDir) throws IOException {
        FileUtils.copyFileToDirectory(srcFile, destDir);
    }
}
