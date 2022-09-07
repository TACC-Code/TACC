class BackupThread extends Thread {
    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            EnyarokClient client = EnyarokClient.get();
            client.connect(serverField.getText(), new Integer(portField.getText()));
            client.login(nameField.getText(), new String(passwordField.getPassword()));
            CharacterFrame frame = CharacterFrame.get();
            StartFrame.get().setVisible(false);
            frame.setVisible(true);
            StartFrame.get().dispose();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(StartFrame.get(), exception.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}
