class BackupThread extends Thread {
    private void saveReportLabelMouseReleased(java.awt.event.MouseEvent evt) {
        File outputFile = null;
        JFileChooser fc = new JFileChooser(peptideShakerGUI.getLastSelectedFolder());
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File myFile) {
                return myFile.getName().toLowerCase().endsWith("txt") || myFile.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Supported formats: Text (.txt)";
            }
        };
        fc.setFileFilter(filter);
        int result = fc.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            outputFile = fc.getSelectedFile();
            if (outputFile.exists()) {
                int choice = JOptionPane.showConfirmDialog(this, new String[] { "The file " + outputFile.getName() + " already exists!", "Overwrite?" }, "File Already Exists", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.NO_OPTION) {
                    return;
                }
            }
        }
        if (outputFile != null) {
            saveReport(outputFile);
        }
    }
}
