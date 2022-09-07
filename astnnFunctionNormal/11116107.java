class BackupThread extends Thread {
    private void copyFileSet(FileSet fileSet, File targetDirectory) throws IOException {
        File dir = fileSet.getDirectory();
        if (dir == null) {
            dir = baseDir;
        }
        File targetDir = targetDirectory;
        if (fileSet.getOutputDirectory() != null) {
            targetDir = new File(targetDir, fileSet.getOutputDirectory());
        }
        if (targetDir.equals(dir)) {
            return;
        }
        DirectoryScanner ds = new DirectoryScanner();
        ds.setBasedir(dir);
        if (!fileSet.isSkipDefaultExcludes()) {
            ds.addDefaultExcludes();
        }
        final String[] excludes = fileSet.getExcludes();
        if (excludes != null) {
            ds.setExcludes(excludes);
        }
        final String[] includes = fileSet.getIncludes();
        if (includes != null) {
            ds.setIncludes(includes);
        }
        ds.scan();
        String[] files = ds.getIncludedFiles();
        for (int i = 0; i < files.length; i++) {
            File sourceFile = new File(dir, files[i]);
            File targetFile = new File(targetDir, files[i]);
            FileUtils.copyFile(sourceFile, targetFile);
        }
    }
}
