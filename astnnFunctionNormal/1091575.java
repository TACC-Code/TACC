class BackupThread extends Thread {
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
}
