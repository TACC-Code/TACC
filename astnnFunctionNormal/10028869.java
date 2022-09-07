class BackupThread extends Thread {
    public void run() {
        CTabItem tab = main.getSelectedTab();
        ScrollingGraphicalViewer viewer = main.getViewer();
        if (viewer != null) {
            String newname;
            boolean again = false;
            do {
                if (again) MessageDialog.openError(main.getShell(), "Error", "Name already exists. Choose another name.");
                again = true;
                StringInputDialog dialog = new StringInputDialog(main.getShell(), "Rename Pathway", "Enter new name", tab.getText());
                dialog.setSelectText(true);
                newname = dialog.open();
            } while (newname != null && !newname.equals(tab.getText()) && (main.getAllPathwayNames().contains(newname) || main.getOpenTabNames().contains(newname)));
            if (newname != null && !newname.equals(tab.getText())) {
                main.renamePathway(tab, newname);
            }
        }
    }
}
