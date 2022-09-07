class BackupThread extends Thread {
    private void processExecAttrs() throws BuildException {
        try {
            for (Iterator execIter = mExecAttrs.iterator(); execIter.hasNext(); ) {
                File src = (File) execIter.next();
                File dest = new File(mMacOsDir, src.getName());
                if (mVerbose) log("Copying exec file to \"" + bundlePath(dest) + "\"");
                mFileUtils.copyFile(src, dest);
                setExecutable(dest);
            }
        } catch (IOException ex) {
            throw new BuildException("Cannot copy exec file: " + ex);
        }
    }
}
