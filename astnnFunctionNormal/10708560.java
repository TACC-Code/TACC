class BackupThread extends Thread {
    private void bConnectarKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) {
            ConnectionConfiguration connConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
            this.connection = new XMPPConnection(connConfig);
            try {
                this.connection.connect();
                this.connection.login(this.tfLogin.getText(), new String(this.pfPassword.getPassword()));
                Presence p = new Presence(Presence.Type.available);
                p.setMode(Presence.Mode.available);
                connection.sendPacket(p);
                this.roster = this.connection.getRoster();
                this.parent.setVisible(false);
                System.out.println("Rosters " + this.roster.getEntryCount());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Connection Error", "Error en la conexión", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
