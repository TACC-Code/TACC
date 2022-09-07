class BackupThread extends Thread {
    public void createFile(StringBuffer stringBuffer) {
        if (lastDirectory == null) {
            lastDirectory = new File(System.getProperty("user.dir"));
        }
        JFileChooser fileChooser = new JFileChooser(lastDirectory);
        ExtensionFileFilter filter = new ExtensionFileFilter("Excel files");
        filter.addExtension(".csv");
        fileChooser.setFileFilter(filter);
        int status = fileChooser.showSaveDialog(this);
        if (status == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.exists()) {
                int result = JOptionPane.showConfirmDialog(this, "This file already exists. Do you want to overwrite it?", "Confirm overwrite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    lastDirectory = selectedFile.getParentFile();
                    try {
                        writeFile(stringBuffer, selectedFile.getAbsolutePath());
                    } catch (Exception e) {
                        logger.info("writeFile() failed to save to file: " + selectedFile.getAbsolutePath(), e);
                    }
                }
            } else {
                lastDirectory = selectedFile.getParentFile();
                try {
                    writeFile(stringBuffer, selectedFile.getAbsolutePath());
                } catch (Exception e) {
                    logger.info("writeFile() failed to save to file: " + selectedFile.getAbsolutePath(), e);
                }
            }
        }
    }
}
