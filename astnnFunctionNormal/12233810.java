class BackupThread extends Thread {
    private void saveXML() {
        JFileChooser chooser = new JFileChooser("config");
        int returnVal = chooser.showSaveDialog(mainFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile().exists()) {
                if (JOptionPane.showConfirmDialog(mainFrame, "File already exists. Overwrite?") == JOptionPane.YES_OPTION) {
                    mainFrame.saveXML(chooser.getSelectedFile());
                }
            } else {
                mainFrame.saveXML(chooser.getSelectedFile());
            }
        }
    }
}
