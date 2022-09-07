class BackupThread extends Thread {
    private int localFileExport(final WorkerFeedback workerFeedback, File sharedWorkingCopy, final File exportTo, final AtomicInteger emptyDirCount, final AtomicLong byteCount) throws IOException {
        final int prefixSize = sharedWorkingCopy.getAbsolutePath().length() + 1;
        final AtomicInteger fileCount = new AtomicInteger();
        final PushingDirectoryTraversal t = new PushingDirectoryTraversal(new FileFilter() {

            private static final long LOGGING_INTERVAL = 5000;

            private long nextLogTime = System.currentTimeMillis() + LOGGING_INTERVAL;

            public boolean accept(File srcFile) {
                final String relpath = srcFile.getAbsolutePath().substring(prefixSize);
                final File targetFile = new File(exportTo, relpath);
                if (srcFile.isDirectory()) {
                    if (srcFile.list().length == 1) {
                        targetFile.mkdirs();
                        emptyDirCount.incrementAndGet();
                        workerFeedback.console(" ... create empty dir: %s", relpath);
                        updateProgress();
                    }
                    return true;
                }
                targetFile.getParentFile().mkdirs();
                try {
                    FileUtils.copyFile(srcFile, targetFile);
                    fileCount.incrementAndGet();
                    byteCount.addAndGet(targetFile.length());
                    updateProgress();
                    return true;
                } catch (IOException e) {
                    throw new IllegalStateException(String.format("problem while copying %s to %s", srcFile, targetFile), e);
                }
            }

            private void updateProgress() {
                if (System.currentTimeMillis() > nextLogTime) {
                    nextLogTime = System.currentTimeMillis() + LOGGING_INTERVAL;
                    workerFeedback.console(" ... copied %d bytes in %d files so far", byteCount.get(), fileCount.get());
                }
            }
        });
        t.addDirectoryExclude(".svn");
        t.setWantDirs(copyEmptyDirs);
        t.setWantFiles(true);
        final long startTime = System.currentTimeMillis();
        t.traverse(sharedWorkingCopy);
        workerFeedback.console("Copied %d files in %d millis", fileCount.get(), System.currentTimeMillis() - startTime);
        if (copyEmptyDirs && emptyDirCount.intValue() > 0) {
            workerFeedback.console("Also created %d empty directories", emptyDirCount.get());
        }
        return fileCount.get();
    }
}
