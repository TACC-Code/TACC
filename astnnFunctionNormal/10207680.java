class BackupThread extends Thread {
    private void handleExport() {
        if (!checkExport()) return;
        String exportPath = txt_exportpath.getText();
        if (exportPath != null && exportPath.trim().length() > 0) {
            if (!exportPath.toLowerCase().endsWith(".zip")) {
                exportPath += ".zip";
            }
            File isDa = new File(exportPath);
            if (isDa.exists()) {
                MessageBox errormessage = new MessageBox(getShell(), SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
                errormessage.setText("Export Warning");
                errormessage.setMessage("The given file already exists. Do you really want to overwrite it?");
                int status = errormessage.open();
                if (status == SWT.CANCEL) return;
            }
            try {
                parent.getContainer().getRessourceManager().saveGLMIMS(exportPath);
                MessageDialog.openInformation(getShell(), "Export", "Export successful!");
            } catch (Exception ex) {
                MessageDialog.openError(getShell(), "Export", ex.getMessage());
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            MessageDialog.openError(getShell(), "Export", "An Export destination has not been specified");
        }
    }
}
