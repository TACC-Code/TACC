class BackupThread extends Thread {
    private void copyDataFiles() throws IOException {
        File destDir = workingDir.getDirectory();
        List<String> files = FileUtils.listRecursively(sourceDir, DashboardInstanceStrategy.INSTANCE.getFilenameFilter());
        for (String filename : files) {
            File srcFile = new File(sourceDir, filename);
            File destFile = new File(destDir, filename);
            RobustFileOutputStream out = new RobustFileOutputStream(destFile);
            FileUtils.copyFile(srcFile, out);
            out.close();
        }
    }
}
