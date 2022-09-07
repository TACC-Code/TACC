class BackupThread extends Thread {
    public File writeToFile(JFrame frame, boolean saveAs) {
        String path = "";
        String extension = "";
        if (savedFile != null && !saveAs) {
            path = savedFile.getPath();
            extension = FileChooserUtils.getExtension(savedFile);
            writeFileHelper(frame, path);
            return savedFile;
        } else {
            JFileChooser fc = new JFileChooser();
            ExtensionFileFilter HTMLFilter = new ExtensionFileFilter("HTML (*.html)", "html");
            fc.addChoosableFileFilter(HTMLFilter);
            fc.setAcceptAllFileFilterUsed(false);
            int returnValue = fc.showSaveDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                path = file.getPath();
                extension = "";
                javax.swing.filechooser.FileFilter filter = fc.getFileFilter();
                if (filter.equals(HTMLFilter)) {
                    extension = "html";
                }
                if (!path.toLowerCase().endsWith("." + extension)) {
                    path += "." + extension;
                    file = new File(path);
                }
                if (file.exists()) {
                    System.out.println("file exists already");
                    Object[] options = { "Overwrite file", "Cancel" };
                    int choice = JOptionPane.showOptionDialog(frame, "The specified file already exists.  Overwrite existing file?", "Overwrite file?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                    if (choice != 0) return null;
                }
                writeFileHelper(frame, path);
                savedFile = file;
                return file;
            }
            return null;
        }
    }
}
