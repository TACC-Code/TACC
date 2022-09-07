class BackupThread extends Thread {
    protected void saveAsDocument(final XalAbstractDocument document) {
        String defaultName = new File(document.getDisplayFilePath()).getName();
        File defaultFolder = _saveFileChooser.getCurrentDirectory();
        File defaultFile = new File(defaultFolder, defaultName);
        _saveFileChooser.setSelectedFile(defaultFile);
        final int status = showSaveFileChooser(document);
        switch(status) {
            case JFileChooser.CANCEL_OPTION:
                break;
            case JFileChooser.APPROVE_OPTION:
                File fileSelection = _saveFileChooser.getSelectedFile();
                if (fileSelection.exists()) {
                    int confirm = document.displayConfirmDialog("Overwrite Confirmation", "The selected file:  " + fileSelection + " already exists! \n Overwrite selection?");
                    if (confirm == NO_OPTION) {
                        saveAsDocument(document);
                        return;
                    }
                }
                saveDocumentToFile(document, fileSelection);
                break;
            case JFileChooser.ERROR_OPTION:
                break;
        }
    }
}
