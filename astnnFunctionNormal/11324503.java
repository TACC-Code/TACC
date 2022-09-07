class BackupThread extends Thread {
        public void save() {
            chooser = new JFileChooser(JXConfig.getProperty("csv.homeDir"));
            chooser.addChoosableFileFilter(new CBFileFilter(new String[] { "csv" }, "CSV Files (*.csv)"));
            int option = chooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File readFile = chooser.getSelectedFile();
                if (readFile == null) {
                    CBUtility.error(CBIntText.get("Please select a file"));
                } else {
                    readFile = adjustFileName(readFile);
                    int response = -1;
                    if (readFile.exists()) {
                        response = JOptionPane.showConfirmDialog(this, CBIntText.get("The File ''{0}'' already exists.  Do you want to replace it?", new String[] { readFile.toString() }), CBIntText.get("Overwrite Confirmation"), JOptionPane.YES_NO_OPTION);
                        if (response != JOptionPane.YES_OPTION) save();
                    }
                    JXConfig.setProperty("csv.homeDir", readFile.getParent());
                    doFileWrite(readFile);
                }
            }
        }
}
