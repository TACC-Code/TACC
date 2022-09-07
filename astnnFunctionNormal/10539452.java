class BackupThread extends Thread {
    protected boolean saveDialog() {
        int overwriteReturnValue = -1;
        returnVal = fc.showSaveDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            if (file.exists()) {
                overwriteReturnValue = JOptionPane.showConfirmDialog(parent, file.toString() + " already exists.\nDo you want to replace it?", "File Already Exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            }
            if (overwriteReturnValue == JOptionPane.NO_OPTION) {
                return false;
            } else return true;
        } else return false;
    }
}
