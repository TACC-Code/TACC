class BackupThread extends Thread {
    private void processCopyingFileLists(List fileLists, File targetDir, boolean setExec) throws BuildException {
        for (Iterator execIter = fileLists.iterator(); execIter.hasNext(); ) {
            FileList fl = (FileList) execIter.next();
            Project p = fl.getProject();
            File srcDir = fl.getDir(p);
            String[] files = fl.getFiles(p);
            if (files.length == 0) {
                System.err.println("WARNING: filelist for copying from directory " + srcDir + ": no files found");
            } else {
                try {
                    for (int i = 0; i < files.length; i++) {
                        String fileName = files[i];
                        File src = new File(srcDir, fileName);
                        File dest = new File(targetDir, fileName);
                        if (mVerbose) log("Copying " + (setExec ? "exec" : "resource") + " file to \"" + bundlePath(dest) + "\"");
                        mFileUtils.copyFile(src, dest);
                        if (setExec) setExecutable(dest);
                    }
                } catch (IOException ex) {
                    throw new BuildException("Cannot copy jar file: " + ex);
                }
            }
        }
    }
}
