class BackupThread extends Thread {
    private File showSaveDialog(String aDefaultDirectory, String aDefaultFileName, JSFileNameExtensionFilter aFilter) {
        File zReturn = null;
        JFileChooser zFileBrowser = getFileBrowser();
        configureFileBrowser(false, false, true, new File(aDefaultDirectory), new File(aDefaultFileName), aFilter);
        boolean zChooseAgain = false;
        do {
            int zResult = zFileBrowser.showSaveDialog(this);
            if (zResult == JFileChooser.APPROVE_OPTION) {
                if (zFileBrowser.getSelectedFile().exists() == true) {
                    int zConfirmResult = JOptionPane.showConfirmDialog(this, "" + zFileBrowser.getSelectedFile().getName() + " already exists.  Do you wish to overwrite?");
                    if (zConfirmResult == JOptionPane.NO_OPTION) {
                        zChooseAgain = true;
                    } else if (zConfirmResult == JOptionPane.YES_OPTION) {
                        zReturn = zFileBrowser.getSelectedFile();
                        zChooseAgain = false;
                    } else {
                        zChooseAgain = false;
                    }
                } else {
                    zReturn = zFileBrowser.getSelectedFile();
                }
            } else {
                zChooseAgain = false;
            }
        } while (zChooseAgain == true);
        return zReturn;
    }
}
