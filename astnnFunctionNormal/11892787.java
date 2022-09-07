class BackupThread extends Thread {
    protected void dumpHTML() {
        try {
            _fileChooser.setSelectedFile(new File(_fileChooser.getCurrentDirectory(), "Untitled.html"));
            int status = _fileChooser.showSaveDialog(this);
            switch(status) {
                case JFileChooser.APPROVE_OPTION:
                    File selectedFile = _fileChooser.getSelectedFile();
                    if (selectedFile.exists()) {
                        int confirm = displayConfirmDialog("Warning", "The selected file:  " + selectedFile + " already exists! \n Overwrite selection?");
                        if (confirm == NO_OPTION) return;
                    } else {
                        selectedFile.createNewFile();
                    }
                    writeHTMLTo(selectedFile);
                    break;
                default:
                    break;
            }
        } catch (Exception exception) {
            displayError("Save Error", "Error saving file: ", exception);
        }
    }
}
