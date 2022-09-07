class BackupThread extends Thread {
    public static void copyDirectoryToDirectory(final File srcDir, final File destDir, final FilenameFilter filter) throws IOException {
        String srcName = FilenameUtils.getName(srcDir.getPath());
        File copied = new File(destDir, srcName);
        copied.mkdir();
        for (File f : srcDir.listFiles(filter)) {
            if (f.isDirectory()) {
                copyDirectoryToDirectory(f, copied, filter);
            } else {
                FileUtils.copyFileToDirectory(f, copied);
            }
        }
    }
}
