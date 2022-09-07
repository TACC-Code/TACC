class BackupThread extends Thread {
        @Override
        public void approveSelection() {
            ExportDiagramAction.this.exportSettingsPanel.saveSettings();
            File selectedFile = getSelectedFile();
            if (selectedFile.getName().indexOf('.') == -1) {
                final FileFilter filter = getFileFilter();
                if (filter instanceof ExtensionFileFilter) {
                    final ExtensionFileFilter extFileFilter = (ExtensionFileFilter) filter;
                    final String[] extensions = extFileFilter.getExtensions();
                    selectedFile = new File(selectedFile.getAbsolutePath() + "." + extensions[0]);
                    setSelectedFile(selectedFile);
                }
            }
            if (selectedFile.exists()) {
                String warningMessage = "The image file '" + selectedFile.getName() + "' already exists.\nDo you want to overwrite the existing file?";
                if (ExportDiagramAction.this.diagramExportSettings.getSaveCommentsToFile() == true) {
                    final File textFile = new File(selectedFile.getAbsoluteFile() + ".txt");
                    if (textFile.exists()) {
                        warningMessage = "The files '" + selectedFile.getName() + "' and '" + textFile.getName() + "' already exist.\nDo you want to overwrite the existing files?";
                    }
                }
                final int response = JOptionPane.showOptionDialog(this, warningMessage, "File Export Warning: File exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[] { "Yes", "No" }, "No");
                if (response != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            if (!selectedFile.exists() && ExportDiagramAction.this.diagramExportSettings.getSaveCommentsToFile() == true) {
                final File textFile = new File(selectedFile.getAbsoluteFile() + ".txt");
                if (textFile.exists()) {
                    final int response = JOptionPane.showOptionDialog(this, "The text file '" + textFile.getName() + "' already exists.\n" + "Do you want to overwrite the existing file?", "File Export Warning: File exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[] { "Yes", "No" }, "No");
                    if (response != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
            }
            super.approveSelection();
        }
}
