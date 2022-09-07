class BackupThread extends Thread {
    private void regBtnActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            String pass = new String(passTF.getPassword());
            String confirmationPass = new String(confirmPassTF.getPassword());
            StringBuffer requiredFields = new StringBuffer("");
            if (loginTF.getText().equals("")) {
                requiredFields.append(REQUIRED_FIELDS_LOGIN);
            }
            if (accessLevelCB.getSelectedIndex() <= Util.FIRST_INDEX) {
                requiredFields.append(REQUIRED_FIELDS_ACCESS_LEVEL);
            }
            if (pass.toString().equals("")) {
                requiredFields.append(REQUIRED_FIELDS_PASS);
            }
            if (confirmationPass.equals("")) {
                requiredFields.append(REQUIRED_FIELDS_CONFIRMATION_PASS);
            }
            if (requiredFields.toString().equals("")) {
                if (!validatePass()) {
                    JOptionPane.showMessageDialog(instance, PASS_DONT_MATCH, TITLE_WARNING, JOptionPane.INFORMATION_MESSAGE);
                    passTF.setText("");
                    confirmPassTF.setText("");
                    return;
                }
                User user = new User();
                user.setLogin(loginTF.getText());
                byte[] b = CriptUtil.digest(pass.getBytes(), CriptUtil.SHA);
                user.setPass(CriptUtil.byteArrayToHexString(b));
                user.setAccessLevel(AccessLevel.get(accessLevelCB.getSelectedItem().toString()));
                facade.saveUser(user);
                JOptionPane.showMessageDialog(instance, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                clearAllFields();
                refresh();
            } else {
                JOptionPane.showMessageDialog(instance, MSG_REQUIRED_FIELDS + requiredFields.toString(), TITLE_WARNING, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        } catch (MyHibernateException ex) {
            Logger.getLogger(RegUserPanel.class.getName()).log(Level.SEVERE, null, ex);
            Util.errorSQLPane(instance, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RegUserPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
