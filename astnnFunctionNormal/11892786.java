class BackupThread extends Thread {
    protected void dumpBuffer() {
        try {
            _fileChooser.setSelectedFile(new File(_fileChooser.getCurrentDirectory(), "Untitled.txt"));
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
                    writeTextTo(selectedFile);
                    break;
                default:
                    break;
            }
        } catch (Exception exception) {
            displayError("Save Error", "Error saving file: ", exception);
        }
    }
}
