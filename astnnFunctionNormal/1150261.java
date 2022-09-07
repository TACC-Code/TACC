class BackupThread extends Thread {
    private boolean foldersValid() {
        if (src == null || dest == null || !src.isDirectory() || !dest.isDirectory() || !src.canRead() || !dest.canWrite()) {
            JOptionPane.showMessageDialog(this, "Source or destination folder not set\n" + "or not a folder\n" + "or source not readable\n" + "or dest not writeable!", "Bad folder", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
