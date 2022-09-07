class BackupThread extends Thread {
    private void processJarFileLists() throws BuildException {
        for (Iterator jarIter = mJarFileLists.iterator(); jarIter.hasNext(); ) {
            FileList fl = (FileList) jarIter.next();
            Project p = fl.getProject();
            File srcDir = fl.getDir(p);
            String[] files = fl.getFiles(p);
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
