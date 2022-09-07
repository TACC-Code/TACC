class BackupThread extends Thread {
    private String export2file() {
        FileDialog dialog = new FileDialog(PluginUtil.getShell(), SWT.SAVE | SWT.SYSTEM_MODAL);
        dialog.setText("Save");
        dialog.setFilterExtensions(new String[] { "*" + ZIP_EXT });
        dialog.setFilterNames(new String[] { "ObjectScript Files (*" + ZIP_EXT + ")" });
        if (lastFileDialogPath == null) dialog.setFilterPath(System.getProperty("user.home") + "\\Desktop"); else dialog.setFilterPath(lastFileDialogPath);
        dialog.setFileName("Untitled");
        final String name = dialog.open();
        if (name == null || name.compareTo("") == 0) {
            return "";
        }
        File file = new File(name);
        if (file.exists()) {
            MessageBox mbox = new MessageBox(parentShell, SWT.OK | SWT.CANCEL | SWT.ICON_WARNING | SWT.APPLICATION_MODAL);
            mbox.setText("Warning");
            mbox.setMessage("This file already exists. Do you want to overwrite it?");
            if (mbox.open() == SWT.CANCEL) return "";
        }
        lastFileDialogPath = dialog.getFilterPath();
        return name;
    }
}
