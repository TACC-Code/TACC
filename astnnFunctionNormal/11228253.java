class BackupThread extends Thread {
    private void fileSaveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        ScriptEditorInternalFrame editor = getActiveEditor();
        if (editor == null) return;
        FrinikaScript script = editor.getScript();
        File file = requester(true);
        if (file != null) {
            try {
                if ((!file.exists()) || frame.confirm("File " + file.getCanonicalPath() + " already exists. Overwrite?")) {
                    engine.saveScript(script, file);
                    editor.updateTitle();
                    editor.lastSaveTimestamp = file.lastModified();
                    editor.setDirty(false);
                }
            } catch (IOException ioe) {
                frame.error(ioe);
            }
        }
    }
}
