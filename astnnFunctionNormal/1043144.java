class BackupThread extends Thread {
    public static File copyToDirectoryUnique(final File srcFile, final File destDir) throws IOException {
        String basename = FilenameUtils.getBaseName(srcFile.getAbsolutePath());
        String extension = StringUtils.lowerCase(FilenameUtils.getExtension(srcFile.getAbsolutePath()));
        File destFile = getUniqueFile(destDir, normalizeFileName(basename) + '.' + extension);
        FileUtils.copyFile(srcFile, destFile);
        return destFile;
    }
}
