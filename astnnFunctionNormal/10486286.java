class BackupThread extends Thread {
    private void btnCreatePasswordRecoveryActionPerform(ActionEvent evt) {
        if (!chkTrust.isSelected()) {
            JOptionPane.showMessageDialog(this, "You should select trusting the server.", "Trust", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int answer = saveDialog.showSaveDialog(this);
        boolean canSave = false;
        if (answer != JFileChooser.CANCEL_OPTION) {
            File file = saveDialog.getSelectedFile();
            if (file.exists()) {
                int answer2 = JOptionPane.showConfirmDialog(this, "The file has already existed. Do you want to rewrite it?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (answer2 == JOptionPane.YES_OPTION) {
                    canSave = true;
                }
            } else {
                canSave = true;
            }
            if (canSave) {
                Domain domain = Domain.getInstance();
                FileWriter writer = null;
                try {
                    String recoveryContent = Utils.createRecoveryContent(domain.getPassword());
                    if (recoveryContent != null) {
                        writer = new FileWriter(file);
                        writer.write(recoveryContent);
                        JOptionPane.showMessageDialog(this, "Recovery file has been created successfully!", "Finish", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        throw new Exception("Recovery Content is null!");
                    }
                } catch (Exception ex) {
                    Logger.log(ex.getMessage());
                    JOptionPane.showMessageDialog(this, "Recovery File creation failed!", "Fatal Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException ex) {
                            Logger.log(ex.getMessage());
                        }
                    }
                }
            }
        }
    }
}
