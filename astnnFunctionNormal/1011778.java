class BackupThread extends Thread {
    public boolean nameAndSaveFile(XmlFile file) {
        XmlFile newFile = editor.getXmlFilesManager().nameNewFile(file);
        if (newFile != null) {
            if (newFile.exists()) {
                Object[] options = { "Yes", "No" };
                int answer = JOptionPane.showOptionDialog(this.getEditor(), "This file already exists.\nDo you want to overwrite it ?", "Existing file", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                if (answer == 1) {
                    return false;
                }
            }
            this.topFileChanged(newFile);
            editor.getSetup().addLastOpenFile(newFile.getAbsolutePath());
            int value = fileList.getRefNumber(file);
            this.removeFile(file);
            fileList.addRef(newFile, value);
            newFile.save();
            return true;
        }
        return false;
    }
}
