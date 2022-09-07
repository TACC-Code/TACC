class BackupThread extends Thread {
    public boolean writeToFile(boolean saveAs) {
        String path = "";
        if (savedFile != null && !saveAs) {
            path = savedFile.getPath();
            writeFileHelper(path, false);
            return true;
        } else {
            JFileChooser fc = new JFileChooser(FileOperations.getRecentProject() == null ? null : FileOperations.getRecentProject().getParentFile());
            ExtensionFileFilter filter = new ExtensionFileFilter("Statcato project file (*.stc)", extension);
            fc.addChoosableFileFilter(filter);
            fc.setAcceptAllFileFilterUsed(false);
            int returnValue = fc.showSaveDialog(app);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                path = file.getPath();
                if (!path.toLowerCase().endsWith("." + extension)) {
                    path += "." + extension;
                    file = new File(path);
                }
                if (file.exists()) {
                    Object[] options = { "Overwrite file", "Cancel" };
                    int choice = JOptionPane.showOptionDialog(app, "The specified file already exists.  Overwrite existing file?", "Overwrite file?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                    if (choice != 0) return false;
                }
                boolean success = writeFileHelper(path, false);
                if (!success) return false;
                savedFile = file;
                return true;
            }
            return false;
        }
    }
}
