class BackupThread extends Thread {
    private void processJarAttrs() throws BuildException {
        try {
            for (Iterator jarIter = mJarAttrs.iterator(); jarIter.hasNext(); ) {
                File src = (File) jarIter.next();
                File dest = new File(mJavaDir, src.getName());
                if (mVerbose) log("Copying JAR file to \"" + bundlePath(dest) + "\"");
                mFileUtils.copyFile(src, dest);
                bundleProperties.addToClassPath(dest.getName());
            }
        } catch (IOException ex) {
            throw new BuildException("Cannot copy jar file: " + ex);
        }
    }
}
