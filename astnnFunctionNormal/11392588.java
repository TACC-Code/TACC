class BackupThread extends Thread {
    @Override
    public void approveSelection() {
        if (getDialogType() == JFileChooser.SAVE_DIALOG) {
            if (getSelectedFile().exists()) {
                int diag = JOptionPane.showConfirmDialog(this, "File already exists. Overwrite ?", "Overwrite file", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (diag == JOptionPane.NO_OPTION) {
                    return;
                }
            }
        }
        super.approveSelection();
    }
}
