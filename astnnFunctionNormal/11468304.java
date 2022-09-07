class BackupThread extends Thread {
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        FileNameExtensionFilter fef = new FileNameExtensionFilter("Portable Network Graphics (PNG)", "png");
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(fef);
        int ret = jfc.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            int ow = JOptionPane.OK_OPTION;
            if (f.exists()) {
                ow = JOptionPane.showConfirmDialog(this, "The file " + f.getName() + " already exists.\nDo you want to overwrite it?");
            }
            if (ow == JOptionPane.OK_OPTION) {
                try {
                    ImageIO.write(img, "png", f);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Couldn't write to: " + f.getName() + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
