class BackupThread extends Thread {
    private void copyServerDir(String subdir) throws Exception {
        File fromDir = new File(jbossCleanDir + sep + subdir + "/");
        File toDir = new File(targetServerDir + sep + subdir + "/");
        FileUtils.copyDirectory(fromDir, toDir);
        for (String subd : new String[] { "bin", "conf", "lib" }) {
            File wrapperBinDirFile = new File(sourceWrapperDir + sep + subd);
            for (File file : FileSystemUtil.getFiles(wrapperBinDirFile.getAbsolutePath())) {
                if (!file.isDirectory()) {
                    FileUtils.copyFile(file, new File(targetServerDir + sep + subd + sep + file.getName()));
                }
            }
        }
    }
}
