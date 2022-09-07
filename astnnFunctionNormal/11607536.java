class BackupThread extends Thread {
    public void fileReceiveRequested(FileTransferSupport connection, Contact contact, String filename, String description, Object connectionInfo) {
        final JFileChooser chooser = new JFileChooser(ClientProperties.INSTANCE.getLastFolder());
        chooser.setDialogTitle(contact + " is sending you file " + filename);
        chooser.setToolTipText(description);
        String name = filename == null ? "" : GeneralUtils.stripHTML(filename);
        name = GeneralUtils.replace(name, "/", "");
        name = GeneralUtils.replace(name, "\\", "");
        chooser.setSelectedFile(new File(ClientProperties.INSTANCE.getLastFolder(), name));
        int returnVal = chooser.showSaveDialog(null);
        ClientProperties.INSTANCE.setLastFolder(chooser.getCurrentDirectory().getAbsolutePath());
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            connection.rejectFileTransfer(connectionInfo);
            return;
        } else if (chooser.getSelectedFile().isDirectory()) {
            JOptionPane.showMessageDialog(Main.getFrame(), "File already exists or is a folder.", "Error:", JOptionPane.ERROR_MESSAGE);
            connection.rejectFileTransfer(connectionInfo);
        } else if (chooser.getSelectedFile().exists()) {
            returnVal = JOptionPane.showConfirmDialog(Main.getFrame(), "File already exists overwrite?.", "Problem:", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (returnVal == JOptionPane.NO_OPTION) {
                fileReceiveRequested(connection, contact, filename, description, connectionInfo);
                return;
            } else if (returnVal == JOptionPane.CANCEL_OPTION) {
                connection.rejectFileTransfer(connectionInfo);
                return;
            }
            if (!chooser.getSelectedFile().delete()) {
                JOptionPane.showMessageDialog(Main.getFrame(), "Failed to delete the file.  Choose a different name please.", "Error:", JOptionPane.ERROR_MESSAGE);
                fileReceiveRequested(connection, contact, filename, description, connectionInfo);
                return;
            }
        }
        connection.acceptFileTransfer(new FileTransferAdapter(Main.getFrame(), description, chooser.getSelectedFile(), contact), connectionInfo);
    }
}
