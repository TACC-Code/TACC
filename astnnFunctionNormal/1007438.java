class BackupThread extends Thread {
    private File showSaveDialog() {
        JFileChooser fileChooser = new JFileChooser(PreferredDirectory.getPreferredDirectory());
        if (fileChooser.showSaveDialog(MaskMaker.this) != JFileChooser.APPROVE_OPTION) return null;
        File f = fileChooser.getSelectedFile();
        if (f == null || (f.exists() && JOptionPane.showConfirmDialog(MaskMaker.this, f.toString() + " already exists. Overwrite ?", "Warning", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)) {
            return null;
        }
        PreferredDirectory.setPreferredDirectory(f);
        return f;
    }
}
