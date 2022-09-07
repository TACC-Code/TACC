class BackupThread extends Thread {
    private void processJarFileSets() throws BuildException {
        for (Iterator jarIter = mJarFileSets.iterator(); jarIter.hasNext(); ) {
            FileSet fs = (FileSet) jarIter.next();
            Project p = fs.getProject();
            File srcDir = fs.getDir(p);
            FileScanner ds = fs.getDirectoryScanner(p);
            fs.setupDirectoryScanner(ds, p);
            ds.scan();
            String[] files = ds.getIncludedFiles();
            try {
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i];
                    File src = new File(srcDir, fileName);
                    File dest = new File(mJavaDir, fileName);
                    if (mVerbose) log("Copying JAR file to \"" + bundlePath(dest) + "\"");
                    mFileUtils.copyFile(src, dest);
                    bundleProperties.addToClassPath(fileName);
                }
            } catch (IOException ex) {
                throw new BuildException("Cannot copy jar file: " + ex);
            }
        }
    }
}
