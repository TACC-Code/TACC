class BackupThread extends Thread {
    protected synchronized void saveDiagramAs() {
        fChooser.setSelectedFile(null);
        int n = fChooser.showSaveDialog(this);
        if (n == 0) {
            String fileName = fChooser.getSelectedFile().getName();
            File file = new File(fChooser.getCurrentDirectory(), fileName);
            String filePath = file.getPath();
            if ((!filePath.endsWith(".xml")) && (!filePath.endsWith(".XML"))) {
                filePath += ".xml";
                file = new File(filePath);
            }
            if (file.exists()) {
                if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, file.getName() + " already exists. Overwrite ?", "Confirm Save As", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
                    return;
                }
            }
            try {
                fCurrentDiagram.setSaved(true);
                fCurrentDiagram.setFile(file);
                saveDiagram();
            } catch (FileException e) {
                JOptionPane.showMessageDialog(fEditor, e.getMessage(), "Save Diagram", JOptionPane.ERROR_MESSAGE);
                fCurrentDiagram.setFile(null);
                fCurrentDiagram.setSaved(false);
                saveDiagramAs();
                return;
            }
        }
        repaint();
    }
}
