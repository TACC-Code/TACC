class BackupThread extends Thread {
        public void approveSelection() {
            boolean doesFileExists = false;
            boolean result = true;
            try {
                doesFileExists = getSelectedFile().exists();
            } catch (Exception e) {
            }
            if (doesFileExists) {
                FilePermission delPermission = new FilePermission(getSelectedFile().getAbsolutePath(), "delete");
                if (checkFilePermission(delPermission)) {
                    String msg = "File " + getSelectedFile() + " is already exists.\n" + "Do you want to overwrite it?";
                    int approveDelete = JOptionPane.showConfirmDialog(null, "File exists!", msg, JOptionPane.YES_NO_OPTION);
                    result = (approveDelete == JOptionPane.YES_OPTION);
                } else {
                    JOptionPane.showMessageDialog(null, "Can not delete file " + getSelectedFile());
                    result = false;
                }
            }
            if (result) {
                super.approveSelection();
            }
        }
}
