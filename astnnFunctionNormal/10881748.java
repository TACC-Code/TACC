class BackupThread extends Thread {
    private void processCopyingFileSets(List fileSets, File targetdir, boolean setExec) {
        for (Iterator execIter = fileSets.iterator(); execIter.hasNext(); ) {
            FileSet fs = (FileSet) execIter.next();
            Project p = fs.getProject();
            File srcDir = fs.getDir(p);
            FileScanner ds = fs.getDirectoryScanner(p);
            fs.setupDirectoryScanner(ds, p);
            ds.scan();
            String[] files = ds.getIncludedFiles();
            if (files.length == 0) {
                System.err.println("WARNING: fileset for copying from directory " + srcDir + ": no files found");
            } else {
                try {
                    for (int i = 0; i < files.length; i++) {
                        String fileName = files[i];
                        File src = new File(srcDir, fileName);
                        File dest = new File(targetdir, fileName);
                        if (mVerbose) log("Copying " + (setExec ? "exec" : "resource") + " file to \"" + bundlePath(dest) + "\"");
                        mFileUtils.copyFile(src, dest);
                        if (setExec) setExecutable(dest);
                    }
                } catch (IOException ex) {
                    throw new BuildException("Cannot copy file: " + ex);
                }
            }
        }
    }
}
